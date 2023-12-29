package cu.suitetecsa.sdk.nauta

import cu.suitetecsa.sdk.nauta.exception.LoginException
import cu.suitetecsa.sdk.nauta.exception.LogoutException
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

object ConnectApi {
    private var communicator: PortalCommunicator = JsoupPortalCommunicator.Builder().build()
    private var scraper = JsoupConnectPortalScraper.Builder().build()

    fun withPortalCommunicator(portalCommunicator: PortalCommunicator) {
        communicator = portalCommunicator
    }

    fun withPortalScraper(portalScraper: ConnectPortalScraper) {
        scraper = portalScraper
    }

    val isConnected: Result<Boolean>
        get() = communicator.performRequest(CheckConnection()) { scraper.parseCheckConnections(it.text) }

    fun getRemainingTime(dataSession: DataSession): Result<Long> =
        isConnected.mapCatching { isConnected ->
            if (!isConnected) {
                throw NautaGetInfoException(
                    "you are not connected. You need to be connected to get the remaining time."
                )
            }

            val action = LoadUserInformation(
                dataSession.username,
                wlanUserIp = dataSession.wlanUserIp,
                csrfHw = dataSession.csrfHw,
                attributeUUID = dataSession.attributeUUID,
                portal = PortalManager.Connect
            )
            communicator.performRequest(action) { it.text.toSeconds() }.getOrThrow()
        }

    private fun init(): Result<Triple<String, String, String>> = runCatching {
        val (loginAction, loginData) = communicator.performRequest(GetPage("https://$connectDomain:8443")) {
            scraper.parseLoginForm(it.text)
        }.getOrThrow()
        Triple(loginAction, loginData["wlanuserip"] ?: "", loginData["CSRFHW"] ?: "")
    }

    fun getUserInformation(username: String, password: String): Result<NautaConnectInformation> =
        runCatching {
            val (_, wlanUserIp, csrfHw) = init().getOrThrow()
            val action = LoadUserInformation(username, password, wlanUserIp, csrfHw, portal = PortalManager.Connect)
            communicator.performRequest(action) { scraper.parseNautaConnectInformation(it.text) }.getOrThrow()
        }

    fun connect(username: String, password: String): Result<DataSession> =
        isConnected.mapCatching { isLoggedIn ->
            val loginExceptionHandler = ExceptionHandler.Builder(LoginException::class.java).build()
            if (isLoggedIn) throw loginExceptionHandler.handleException("Fail to connect", listOf("Already connected"))

            val (_, wlanUserIp, csrfHw) = init().getOrThrow()

            val action = Login(csrfHw, wlanUserIp, username, password, portal = PortalManager.Connect)
            val uuid = communicator.performRequest(action) { scraper.parseAttributeUUID(it.text) }.getOrThrow()
            DataSession(username, csrfHw, wlanUserIp, uuid)
        }

    fun disconnect(dataSession: DataSession): Result<Unit> =
        isConnected.mapCatching { connected ->
            val logoutExceptionHandler = ExceptionHandler.Builder(LogoutException::class.java).build()
            if (!connected) {
                throw logoutExceptionHandler.handleException("Fail to disconnect", listOf("You are not connected"))
            }

            val action = Logout(
                dataSession.username,
                dataSession.wlanUserIp,
                dataSession.csrfHw,
                dataSession.attributeUUID
            )
            val isLogout = communicator.performRequest(action) { scraper.isSuccessLogout(it.text) }.getOrThrow()
            if (!isLogout) throw logoutExceptionHandler.handleException("Failed to disconnect", listOf(""))
        }
}
