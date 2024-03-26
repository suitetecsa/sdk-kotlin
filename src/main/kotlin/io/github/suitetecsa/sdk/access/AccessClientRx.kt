package io.github.suitetecsa.sdk.access

import io.github.suitetecsa.sdk.access.exception.LoadInfoException
import io.github.suitetecsa.sdk.access.exception.LogoutException
import io.github.suitetecsa.sdk.access.model.ConnectInformation
import io.github.suitetecsa.sdk.access.model.DataSession
import io.github.suitetecsa.sdk.access.network.PortalCommunicator
import io.github.suitetecsa.sdk.access.scraper.ConnectionInfoParser
import io.github.suitetecsa.sdk.exception.LoginException
import io.github.suitetecsa.sdk.exception.NautaAttributeException
import io.github.suitetecsa.sdk.exception.NautaException
import io.reactivex.rxjava3.core.Observable
import org.jetbrains.annotations.Contract

class AccessClientRx private constructor(
    communicator: PortalCommunicator,
    scraper: ConnectionInfoParser
) {
    private val accessClient = AccessClient.Builder().withCommunicator(communicator).withScraper(scraper).build()

    /**
     * Verifica si hay conexión a internet
     *
     * @return true si hay conexión, false en caso contrario
     * @throws NautaAttributeException sí hay un error obteniendo los atributos
     * @throws NautaException sí hay un error de conexión general
     * @throws LoadInfoException sí hay un error cargando la información
     */
    @get:Throws(NautaAttributeException::class, NautaException::class, LoadInfoException::class)
    val isConnected: Observable<Boolean> = Observable.fromCallable { accessClient.isConnected }

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
    fun getRemainingTime(dataSession: DataSession): Observable<String> = Observable.fromCallable {
        accessClient.getRemainingTime(dataSession)
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
    fun getUserInformation(username: String, password: String): Observable<ConnectInformation> =
        Observable.fromCallable { accessClient.getUserInformation(username, password) }

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
    fun connect(username: String, password: String): Observable<DataSession> =
        Observable.fromCallable { accessClient.connect(username, password) }

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
    fun disconnect(dataSession: DataSession) = Observable.fromCallable { accessClient.disconnect(dataSession) }

    class Builder {
        private var communicator: PortalCommunicator? = null
        private var scraper: ConnectionInfoParser? = null

        fun withCommunicator(communicator: PortalCommunicator) = apply { this.communicator = communicator }
        fun withScraper(scraper: ConnectionInfoParser) = apply { this.scraper = scraper }

        fun build(): AccessClientRx {
            return AccessClientRx(
                communicator ?: PortalCommunicator.Builder().build(),
                scraper ?: ConnectionInfoParser.Builder().build()
            )
        }
    }
}
