package cu.suitetecsa.sdk.nauta

import cu.suitetecsa.sdk.nauta.exception.LoginException
import cu.suitetecsa.sdk.nauta.exception.LogoutException
import cu.suitetecsa.sdk.nauta.exception.NautaAttributeException
import cu.suitetecsa.sdk.nauta.exception.NautaGetInfoException
import cu.suitetecsa.sdk.nauta.model.DataSession
import cu.suitetecsa.sdk.nauta.model.NautaConnectInformation
import cu.suitetecsa.sdk.nauta.scraper.ConnectPortalScraper
import cu.suitetecsa.sdk.nauta.scraper.JsoupConnectPortalScraper
import cu.suitetecsa.sdk.nauta.util.PortalManager
import cu.suitetecsa.sdk.nauta.util.action.CheckConnection
import cu.suitetecsa.sdk.nauta.util.action.GetPage
import cu.suitetecsa.sdk.nauta.util.action.LoadUserInformation
import cu.suitetecsa.sdk.nauta.util.action.Login
import cu.suitetecsa.sdk.nauta.util.action.Logout
import cu.suitetecsa.sdk.nauta.util.connectDomain
import cu.suitetecsa.sdk.nauta.util.toSeconds
import cu.suitetecsa.sdk.network.JsoupPortalCommunicator
import cu.suitetecsa.sdk.network.PortalCommunicator
import cu.suitetecsa.sdk.util.ExceptionHandler

class ConnectApi private constructor(
    private val communicator: PortalCommunicator,
    private val scraper: ConnectPortalScraper
) {
    var username: String? = null
        private set
    var password: String? = null
        private set

    private var actionLogin: String? = null
    private var wlanUserIp: String? = null
    private var csrfHw: String? = null
    private var attributeUUID: String? = null

    val isConnected: Result<Boolean>
        get() = communicator.performAction(CheckConnection()) { scraper.parseCheckConnections(it.text) }

    var dataSession: DataSession? = null
        set(value) {
            field = value
            username = value?.username
            csrfHw = value?.csrfHw
            wlanUserIp = value?.wlanUserIp
            attributeUUID = value?.attributeUUID
        }

    val remainingTime: Result<Long>
        get() = isConnected.mapCatching { isConnected ->
            if (!isConnected) {
                throw NautaGetInfoException(
                    "you are not connected. You need to be connected to get the remaining time."
                )
            }
            checkDataSession()
            checkCredentials()

            val action = LoadUserInformation(
                username,
                wlanUserIp = wlanUserIp,
                csrfHw = csrfHw,
                attributeUUID = attributeUUID,
                portal = PortalManager.Connect
            )
            communicator.performAction(action) { it.text.toSeconds() }.getOrThrow()
        }

    val connectInformation: Result<NautaConnectInformation>
        get() = runCatching {
            checkCredentials()
            if (csrfHw.isNullOrBlank()) init()
            val action = LoadUserInformation(username, password, wlanUserIp, csrfHw, portal = PortalManager.Connect)
            communicator.performAction(action) { scraper.parseNautaConnectInformation(it.text) }.getOrThrow()
        }

    private fun checkCredentials() {
        if (username.isNullOrBlank() || password.isNullOrBlank()) {
            throw NautaAttributeException("username and password are required")
        }
    }

    private fun checkDataSession() {
        if (dataSession == null) throw NautaAttributeException("dataSession cannot be null")
    }

    private fun init() {
        val (loginAction, loginData) = communicator.performAction(GetPage("https://$connectDomain")) {
            scraper.parseLoginForm(it.text)
        }.getOrThrow()
        wlanUserIp = loginData["wlanuserip"] ?: ""
        csrfHw = loginData["CSRFHW"] ?: ""
        actionLogin = loginAction
    }

    fun setCredentials(username: String, password: String) {
        this.username = username
        this.password = password
    }

    fun connect(): Result<String> =
        isConnected.mapCatching { isLoggedIn ->
            val loginExceptionHandler = ExceptionHandler.Builder(LoginException::class.java).build()
            if (isLoggedIn) throw loginExceptionHandler.handleException("Fail to connect", listOf("Already connected"))
            checkCredentials()
            init()

            val action = Login(csrfHw, wlanUserIp, username!!, password!!, portal = PortalManager.Connect)
            val uuid = communicator.performAction(action) { scraper.parseAttributeUUID(it.text) }.getOrThrow()
            dataSession = DataSession(username!!, csrfHw!!, wlanUserIp!!, uuid)
            "Connected"
        }

    fun disconnect(): Result<String> =
        isConnected.mapCatching { connected ->
            val logoutExceptionHandler = ExceptionHandler.Builder(LogoutException::class.java).build()
            if (!connected) {
                throw logoutExceptionHandler.handleException("Fail to disconnect", listOf("You are not connected"))
            }
            checkDataSession()

            val action = Logout(username!!, password!!, csrfHw!!, attributeUUID!!)
            val isLogout = communicator.performAction(action) { scraper.isSuccessLogout(it.text) }.getOrThrow()
            if (!isLogout) throw logoutExceptionHandler.handleException("Failed to disconnect", listOf(""))
            dataSession = null
            "Disconnected"
        }

    class Builder {
        private var communicator: PortalCommunicator? = null
        private var scraper: ConnectPortalScraper? = null

        fun withCommunicator(communicator: PortalCommunicator): Builder {
            this.communicator = communicator
            return this
        }

        fun withScraper(scraper: ConnectPortalScraper): Builder {
            this.scraper = scraper
            return this
        }

        fun build(): ConnectApi {
            return ConnectApi(
                communicator ?: JsoupPortalCommunicator.Builder().build(),
                scraper ?: JsoupConnectPortalScraper.Builder().build()
            )
        }
    }
}
