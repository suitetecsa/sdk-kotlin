package cu.suitetecsa.sdk.nauta

import cu.suitetecsa.sdk.nauta.exception.LoginException
import cu.suitetecsa.sdk.nauta.exception.NautaAttributeException
import cu.suitetecsa.sdk.nauta.exception.NotLoggedInException
import cu.suitetecsa.sdk.nauta.model.NautaUser
import cu.suitetecsa.sdk.nauta.network.UserPortalSession
import cu.suitetecsa.sdk.nauta.scraper.AuthUserPortalScraper
import cu.suitetecsa.sdk.nauta.scraper.JsoupAuthUserPortalScrapper
import cu.suitetecsa.sdk.nauta.util.PortalManager
import cu.suitetecsa.sdk.nauta.util.action.GetCaptcha
import cu.suitetecsa.sdk.nauta.util.action.LoadUserInformation
import cu.suitetecsa.sdk.nauta.util.action.Login
import cu.suitetecsa.sdk.network.Action
import cu.suitetecsa.sdk.network.HttpMethod
import cu.suitetecsa.sdk.network.JsoupPortalCommunicator
import cu.suitetecsa.sdk.network.PortalCommunicator
import cu.suitetecsa.sdk.util.ExceptionHandler

/**
 * This class is the entry point for the user portal authentication API
 * @property isNautaHome whether the user has logged in with an account associated with Nauta Home (Default: false)
 */
class UserPortalAuthApi private constructor(
    private val communicator: PortalCommunicator,
    private val scraper: AuthUserPortalScraper
) {
    private var username = ""
    private var password = ""
    private var csrf = ""

    var isNautaHome: Boolean = false
        private set

    /**
     * Set credentials for the user portal authentication API
     *
     * @param username the username
     * @param password the password
     *
     * @return the user portal authentication API with credentials assigned
     *
     * @throws NautaAttributeException if username or password is blank
     */
    fun setCredentials(username: String, password: String) = apply {
        val attributeExceptionHandle = ExceptionHandler
            .Builder(NautaAttributeException::class.java)
            .build()
        if (username.isBlank()) throw attributeExceptionHandle.handleException("username is required", listOf(""))
        if (password.isBlank()) throw attributeExceptionHandle.handleException("password is required", listOf(""))
        this.username = username
        this.password = password
    }

    /**
     * Load the captcha image for the user portal authentication API
     * @return the captcha image
     */
    val captchaImage: Result<ByteArray>
        get() = communicator.performAction(GetCaptcha) { it.content }

    /**
     * Load the user information for the user portal authentication API
     * @return the user information
     * @throws NotLoggedInException if the user is not logged in
     */
    val userInformation: Result<NautaUser>
        get() = runCatching {
            if (csrf.isBlank()) throw NotLoggedInException("Failed to get user information :: You are not logged in")
            communicator.performAction(LoadUserInformation(portal = PortalManager.User)) {
                scraper.parseNautaUser(it.text)
            }.getOrThrow().apply { isNautaHome = !offer.isNullOrEmpty() }
        }

    private fun loadCsrf(action: Action) {
        csrf = communicator.performAction(action.url) {
            scraper.parseCsrfToken(it.text)
        }.getOrThrow()
    }

    /**
     * Login to the user portal authentication API
     * @param captchaCode the captcha code
     * @return the user information
     */
    fun login(captchaCode: String): Result<NautaUser> = runCatching {
        val action = Login(
            username = username,
            password = password,
            captchaCode = captchaCode,
            portal = PortalManager.User,
            method = HttpMethod.GET
        )
        if (csrf.isBlank()) loadCsrf(action)
        communicator.performAction(action.copy(csrf = csrf, method = HttpMethod.POST)) {
            scraper.parseNautaUser(it.text, ExceptionHandler.Builder(LoginException::class.java).build())
        }.getOrThrow().apply { isNautaHome = !offer.isNullOrEmpty() }
    }

    /**
     * Builder for the user portal authentication API
     */
    class Builder {
        private var communicator: PortalCommunicator? = null
        private var scraper: AuthUserPortalScraper? = null

        /**
         * Set the communicator for the user portal authentication API
         * @param communicator the communicator
         * @return the builder
         */
        fun withCommunicator(communicator: PortalCommunicator) = apply { this.communicator = communicator }

        /**
         * Set the scraper for the user portal authentication API
         * @param scraper the scraper
         * @return the builder
         */
        fun withScraper(scraper: AuthUserPortalScraper) = apply { this.scraper = scraper }

        /**
         * Build the user portal authentication API
         * @return the user portal authentication API
         */
        fun build(): UserPortalAuthApi = UserPortalAuthApi(
            communicator ?: JsoupPortalCommunicator
                .Builder()
                .withSession(UserPortalSession)
                .build(),
            scraper ?: JsoupAuthUserPortalScrapper
        )
    }
}
