package cu.suitetecsa.sdk.nauta.scraper

import cu.suitetecsa.sdk.nauta.util.PortalManager
import cu.suitetecsa.sdk.nauta.util.throwExceptionOnFailure
import cu.suitetecsa.sdk.util.ExceptionHandler
import org.jsoup.Jsoup

/**
 * Implementación de `ErrorParser` que analiza mensajes de error en contenido HTML.
 */
internal object JsoupErrorParser : ErrorParser {
    /**
     * Analiza el HTML para extraer mensajes de error y encapsularlos en un objeto `ResultType`.
     *
     * @param html El contenido HTML a analizar.
     * @param message El mensaje de error por defecto.
     * @param exceptionHandler El gestor de excepciones a utilizar.
     * @return Un objeto `ResultType` que contiene el mensaje de error o éxito.
     */
    @Suppress("TooGenericExceptionCaught")
    override fun parseErrors(html: String, message: String, exceptionHandler: ExceptionHandler): Result<String> =
        runCatching {
            html.also { Jsoup.parse(it).throwExceptionOnFailure(message, PortalManager.User, exceptionHandler) }
        }
}
