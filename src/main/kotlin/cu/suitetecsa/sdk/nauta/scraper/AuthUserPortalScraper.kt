package cu.suitetecsa.sdk.nauta.scraper

/**
 * Interfaz que define un scraper para analizar información en contenido HTML relacionado con el portal de usuario.
 */
interface AuthUserPortalScraper : ErrorParser, TokenParser, UserInfoParser
