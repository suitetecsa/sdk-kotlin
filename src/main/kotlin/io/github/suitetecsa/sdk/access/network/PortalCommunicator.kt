package io.github.suitetecsa.sdk.access.network

import io.github.suitetecsa.sdk.access.exception.LoadInfoException
import io.github.suitetecsa.sdk.exception.NautaException
import io.github.suitetecsa.sdk.exception.NautaAttributeException
import java.util.function.Function

/**
 * Esta interfaz define un comunicador para interactuar con el portal de conexión.
 * Permite realizar acciones y transformar las respuestas utilizando un transformador personalizado.
 */
interface PortalCommunicator {
    /**
     * Realiza una acción en el portal de conexión y transforma la respuesta según la función dada.
     *
     * @param route La acción que se va a realizar en el portal.
     * @param transform La función de transformación que se aplicará a la respuesta del portal.
     * @param <T> El tipo de objeto que se espera como resultado.
     * @return Objeto `ResultType` que encapsula el resultado de la acción realizada y transformada.
     </T> */
    @Throws(NautaAttributeException::class, NautaException::class, LoadInfoException::class)
    fun <T> performRequest(route: AccessRoute, transform: Function<HttpResponse, T>): T

    /**
     * Realiza una acción en el portal de conexión y transforma la respuesta según la función dada.
     *
     * @param url La URL a la que se va a realizar la solicitud.
     * @param transform La función de transformación que se aplicará a la respuesta del portal.
     * @param <T> El tipo de objeto que se espera como resultado.
     * @return Objeto `ResultType` que encapsula el resultado de la acción realizada y transformada.
     </T> */
    @Throws(NautaException::class, LoadInfoException::class)
    fun <T> performRequest(url: String, transform: Function<HttpResponse, T>): T

    class Builder {
        private var session: Session? = null

        fun withSession(session: Session?): Builder {
            this.session = session
            return this
        }

        fun build(): PortalCommunicator = PortalCommunicatorImpl(session ?: Session.Builder().build())
    }
}
