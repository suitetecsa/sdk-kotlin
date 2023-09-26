package cu.suitetecsa.sdk.nauta.scraper

import cu.suitetecsa.sdk.nauta.model.NautaUser
import cu.suitetecsa.sdk.util.ExceptionHandler

interface UserInfoParser : ErrorParser {
    /**
     * Analiza el HTML para extraer la información del usuario de Nauta.
     *
     * @param html El contenido HTML a analizar.
     * @param exceptionHandler El gestor de excepciones a utilizar.
     * @return El objeto `NautaUser` que contiene la información del usuario.
     */
    fun parseNautaUser(html: String, exceptionHandler: ExceptionHandler? = null): NautaUser
}
