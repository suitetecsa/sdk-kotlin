package cu.suitetecsa.sdk.nauta.scraper

interface TokenParser {
    /**
     * Analiza el HTML para extraer el token CSRF.
     *
     * @param html El contenido HTML a analizar.
     * @return El token CSRF.
     */
    fun parseCsrfToken(html: String): String
}
