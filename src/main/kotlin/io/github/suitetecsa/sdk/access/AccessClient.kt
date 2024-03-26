package io.github.suitetecsa.sdk.access

import io.github.suitetecsa.sdk.access.exception.LoadInfoException
import io.github.suitetecsa.sdk.access.exception.LogoutException
import io.github.suitetecsa.sdk.access.model.ConnectInformation
import io.github.suitetecsa.sdk.access.model.DataSession
import io.github.suitetecsa.sdk.access.network.AccessRoute
import io.github.suitetecsa.sdk.access.network.HttpResponse
import io.github.suitetecsa.sdk.access.network.PortalCommunicator
import io.github.suitetecsa.sdk.access.scraper.ConnectionInfoParser
import io.github.suitetecsa.sdk.access.scraper.ErrorParser
import io.github.suitetecsa.sdk.access.scraper.FormParser
import io.github.suitetecsa.sdk.access.utils.ExceptionHandler
import io.github.suitetecsa.sdk.exception.LoginException
import io.github.suitetecsa.sdk.exception.NautaAttributeException
import io.github.suitetecsa.sdk.exception.NautaException
import org.jetbrains.annotations.Contract

class AccessClient private constructor(
    private val communicator: PortalCommunicator,
    private val scraper: ConnectionInfoParser
) {
    private fun init(): Array<String> {
        val scraper = FormParser.Builder().build()

        val loginResponse = communicator.performRequest(AccessRoute.Init, scraper::parseLoginForm)
        val loginAction = loginResponse.key
        val loginData = loginResponse.value

        val wlanUserIp = loginData.getOrDefault("wlanuserip", "")
        val csrfHW = loginData.getOrDefault("CSRFHW", "")

        return arrayOf(loginAction, wlanUserIp, csrfHW)
    }

    /**
     * Verifica si hay conexión a internet
     *
     * @return true si hay conexión, false en caso contrario
     * @throws NautaAttributeException sí hay un error obteniendo los atributos
     * @throws NautaException sí hay un error de conexión general
     * @throws LoadInfoException sí hay un error cargando la información
     */
    @get:Throws(NautaAttributeException::class, NautaException::class, LoadInfoException::class)
    val isConnected: Boolean
        get() = communicator.performRequest(AccessRoute.CheckAccess, scraper::parseCheckConnection)

    /**
     * Obtiene el tiempo restante de la sesión
     *
     * @param dataSession datos de la sesión
     * @return el tiempo restante en segundos
     * @throws NautaAttributeException si hay error obteniendo los atributos
     * @throws NautaException si hay error de conexión
     * @throws LoadInfoException si no hay conexión
     */
    @Throws(NautaAttributeException::class, NautaException::class, LoadInfoException::class)
    fun getRemainingTime(dataSession: DataSession): String {
        if (!isConnected) {
            throw LoadInfoException("you are not connected. You need to be connected to get the remaining accountTime.")
        }

        val route = AccessRoute.GetTime(
            username = dataSession.username,
            wlanUserIp = dataSession.wlanUserIp,
            csrfHw = dataSession.csrfHw,
            attributeUUID = dataSession.attributeUUID
        )
        return communicator.performRequest(route, scraper::parseRemainingTime)
    }

    /**
     * Obtiene la información de conexión del usuario
     *
     * @param username el nombre de usuario
     * @param password la contraseña
     * @return la información de conexión
     * @throws NautaAttributeException si hay error obteniendo atributos
     * @throws NautaException si hay error de conexión
     * @throws LoadInfoException sí hay error cargando la información
     */
    @Throws(NautaAttributeException::class, NautaException::class, LoadInfoException::class)
    fun getUserInformation(username: String, password: String): ConnectInformation {
        val initResult = init()
        val wlanUserIp = initResult[1]
        val csrfHw = initResult[2]

        val route = AccessRoute.UserInfo(
            username = username,
            password = password,
            wlanUserIp = wlanUserIp,
            csrfHw = csrfHw
        )
        return communicator.performRequest(route, scraper::parseConnectInformation)
    }

    /**
     * Realiza la conexión a internet con las credenciales dadas
     *
     * @param username el nombre de usuario
     * @param password la contraseña
     * @return la sesión de datos de la conexión
     * @throws NautaAttributeException si hay error obteniendo atributos
     * @throws NautaException si hay error de conexión
     * @throws LoadInfoException sí hay error cargando información
     * @throws LoginException si falla el login
     */
    @Contract("_, _ -> new")
    @Throws(NautaAttributeException::class, NautaException::class, LoadInfoException::class, LoginException::class)
    fun connect(username: String, password: String): DataSession {
        val loginExceptionHandler = ExceptionHandler { LoginException(it) }
        if (isConnected) {
            throw loginExceptionHandler.handleException("Fail to connect", listOf("Already connected"))
        }

        val initResult = init()
        val wlanUserIp = initResult[1]
        val csrfHw = initResult[2]

        val route =
            AccessRoute.Connect(username = username, password = password, csrfHw = csrfHw, wlanUserIp = wlanUserIp)
        val uuid = communicator.performRequest(route) { httpResponse: HttpResponse ->
            ErrorParser.Builder().build().throwExceptionOnFailure(
                httpResponse,
                "Fail to login",
                loginExceptionHandler
            )
            scraper.parseAttributeUUID(httpResponse)
        }
        return DataSession(username, csrfHw, wlanUserIp, uuid)
    }

    /**
     * Desconecta la sesión actual
     *
     * @param dataSession datos de la sesión a desconectar
     * @throws NautaAttributeException si hay error obteniendo atributos
     * @throws NautaException si hay error de conexión
     * @throws LoadInfoException si no hay conexión
     * @throws LogoutException si falla el logout
     */
    @Throws(NautaAttributeException::class, NautaException::class, LoadInfoException::class, LogoutException::class)
    fun disconnect(dataSession: DataSession) {
        val logoutExceptionHandler = ExceptionHandler { LogoutException(it) }
        if (!isConnected) {
            throw logoutExceptionHandler.handleException("Fail to disconnect", listOf("You are not connected"))
        }

        val communicator = PortalCommunicator.Builder().build()
        val scraper = ConnectionInfoParser.Builder().build()

        val route = AccessRoute.Disconnect(
            username = dataSession.username,
            wlanUserIp = dataSession.wlanUserIp,
            csrfHw = dataSession.csrfHw,
            attributeUUID = dataSession.attributeUUID
        )
        val isLogout = communicator.performRequest(route, scraper::isSuccessLogout)
        if (!isLogout) throw logoutExceptionHandler.handleException("Failed to disconnect", listOf(""))
    }

    class Builder {
        private var communicator: PortalCommunicator? = null
        private var scraper: ConnectionInfoParser? = null

        fun withCommunicator(communicator: PortalCommunicator) = apply { this.communicator = communicator }
        fun withScraper(scraper: ConnectionInfoParser) = apply { this.scraper = scraper }

        fun build(): AccessClient {
            return AccessClient(
                communicator ?: PortalCommunicator.Builder().build(),
                scraper ?: ConnectionInfoParser.Builder().build()
            )
        }
    }
}
