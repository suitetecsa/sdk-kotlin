package cu.suitetecsa.sdk.nauta.scraper

import cu.suitetecsa.sdk.util.ExceptionHandler

/**
 * Interfaz que define un parser para analizar mensajes de error en contenido HTML.
 */
interface ErrorParser {
    /**
     * Analiza el HTML para extraer mensajes de error y encapsularlos en un objeto `ResultType`.
     *
     * @param html El contenido HTML a analizar.
     * @param message El mensaje de error por defecto.
     * @param exceptionHandler El gestor de excepciones a utilizar.
     * @return Un objeto `ResultType` que contiene el mensaje de error o éxito.
     */
    fun parseErrors(
        html: String,
        message: String = "nothing",
        exceptionHandler: ExceptionHandler = ExceptionHandler.Builder().build()
    ): Result<String>
}
