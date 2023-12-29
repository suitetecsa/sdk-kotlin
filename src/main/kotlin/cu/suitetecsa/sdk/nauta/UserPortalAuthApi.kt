package cu.suitetecsa.sdk.nauta

import cu.suitetecsa.sdk.nauta.exception.LoginException
import cu.suitetecsa.sdk.nauta.exception.NotLoggedInException
import cu.suitetecsa.sdk.nauta.model.NautaUser
import cu.suitetecsa.sdk.nauta.scraper.AuthUserPortalScraper
import cu.suitetecsa.sdk.nauta.scraper.JsoupAuthUserPortalScrapper
import cu.suitetecsa.sdk.nauta.util.PortalManager
import cu.suitetecsa.sdk.nauta.util.action.GetCaptcha
import cu.suitetecsa.sdk.nauta.util.action.LoadUserInformation
import cu.suitetecsa.sdk.nauta.util.action.Login
import cu.suitetecsa.sdk.network.HttpMethod
import cu.suitetecsa.sdk.util.ExceptionHandler

/**
 * This class is the entry point for the user portal authentication API
 */
class UserPortalAuthApi private constructor(
    private val sessionManager: UserPortalSessionManager,
    private val scraper: AuthUserPortalScraper,
) {
    private var csrf = ""

    /**
     * Load the captcha image for the user portal authentication API
     * @return the captcha image
     */
    val captchaImage: Result<ByteArray>
        get() = sessionManager.communicator.performRequest(GetCaptcha) { it.content }

    /**
     * Load the user information for the user portal authentication API
     * @return the user information
     * @throws NotLoggedInException if the user is not logged in
     */
    val userInformation: Result<NautaUser>
        get() = runCatching {
            if (csrf.isBlank()) throw NotLoggedInException("Failed to get user information :: You are not logged in")
            sessionManager.communicator.performRequest(LoadUserInformation(portal = PortalManager.User)) {
                scraper.parseNautaUser(it.text)
            }.getOrThrow().apply { sessionManager.isNautaHome = !offer.isNullOrEmpty() }
        }

    /**
     * Login to the user portal authentication API
     * @param captchaCode the captcha code
     * @return the user information
     */
    fun login(username: String, password: String, captchaCode: String): Result<NautaUser> = runCatching {
        val action = Login(
            username = username,
            password = password,
            captchaCode = captchaCode,
            portal = PortalManager.User,
            method = HttpMethod.GET
        )
        if (csrf.isBlank()) csrf = sessionManager.loadCsrf(action)
        sessionManager.communicator.performRequest(action.copy(csrf = csrf, method = HttpMethod.POST)) {
            scraper.parseNautaUser(it.text, ExceptionHandler.Builder(LoginException::class.java).build())
        }.getOrThrow().apply {
            sessionManager.isNautaHome = !offer.isNullOrEmpty()
            sessionManager.sessionOwner = username
        }
    }

    /**
     * Builder for the user portal authentication API
     */
    class Builder {
        private var sessionManager: UserPortalSessionManager? = null
        private var scraper: AuthUserPortalScraper? = null

        /**
         * Set the communicator for the user portal authentication API
         * @param sessionManager the communicator
         * @return the builder
         */
        fun withSessionManager(sessionManager: UserPortalSessionManager) =
            apply { this.sessionManager = sessionManager }

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
            sessionManager ?: DefaultUserPortalSessionManager
                .Builder()
                .build(),
            scraper ?: JsoupAuthUserPortalScrapper
        )
    }
}
