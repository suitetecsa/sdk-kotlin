package cu.suitetecsa.sdk.nauta.util

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Utility class for parsing error messages from an HTML document.
 *
 * @param portalManager The portal manager associated with the error messages.
 */
object HtmlErrorParser {
    private var portalManager: PortalManager = PortalManager.User

    private val regex get() = when (portalManager) {
        PortalManager.Connect -> """alert\("(?<reason>[^"]*?)"\)""".toRegex()
        PortalManager.User -> """toastr\.error\('(?<reason>.*)'\)""".toRegex()
    }

    fun whenPortalManager(portalManager: PortalManager) = apply { this.portalManager = portalManager }

    /**
     * Parses the error message from the given HTML document.
     *
     * @param document The HTML document to parse.
     * @return The parsed error message, or null if no error message is found.
     */
    fun parseError(document: Document): String? {
        val lastScript = document.select("script[type='text/javascript']").lastOrNull()
        return lastScript?.let { script ->
            val reason = regex.find(script.data().trim())?.groups?.get("reason")?.value
            reason?.let { Jsoup.parse(it).selectFirst("li[class=\"msg_error\"]")?.text() }
        }
    }
}
