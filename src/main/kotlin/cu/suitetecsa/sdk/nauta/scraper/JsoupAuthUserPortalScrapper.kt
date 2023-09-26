package cu.suitetecsa.sdk.nauta.scraper

import cu.suitetecsa.sdk.nauta.exception.LoadInfoException
import cu.suitetecsa.sdk.nauta.model.NautaUser
import cu.suitetecsa.sdk.nauta.util.PortalManager
import cu.suitetecsa.sdk.nauta.util.throwExceptionOnFailure
import cu.suitetecsa.sdk.util.ExceptionHandler
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

internal object JsoupAuthUserPortalScrapper : AuthUserPortalScraper {

    private val loadInfoExceptionHandler = ExceptionHandler.Builder(LoadInfoException::class.java).build()

    @Suppress("TooGenericExceptionCaught")
    override fun parseErrors(html: String, message: String, exceptionHandler: ExceptionHandler): Result<String> {
        val htmlParsed = Jsoup.parse(html)
        return try {
            htmlParsed.throwExceptionOnFailure(message, PortalManager.User, exceptionHandler)
            Result.success(html)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun parseCsrfToken(html: String): String {
        val htmlParsed = Jsoup.parse(html)
        htmlParsed.throwExceptionOnFailure("Fail to parse csrf token", PortalManager.User, loadInfoExceptionHandler)
        return htmlParsed.selectFirst("input[name=csrf]")?.attr("value") ?: ""
    }

    override fun parseNautaUser(html: String, exceptionHandler: ExceptionHandler?): NautaUser {
        val htmlParsed = Jsoup.parse(html)
        htmlParsed.throwExceptionOnFailure(
            "Fail to parsing user information",
            PortalManager.User,
            exceptionHandler ?: loadInfoExceptionHandler
        )
        return parseUserAttributes(htmlParsed)
    }

    @Suppress("MagicNumber")
    private fun parseUserAttributes(htmlParsed: Document): NautaUser {
        val attrs = htmlParsed.selectFirst(".z-depth-1")!!.select(".m6")
        val attrList = attrs.map { it.selectFirst("p")!!.text().trim() }
        return NautaUser(
            attrList[0], attrList[1], attrList[2], attrList[3], attrList[4], attrList[5], attrList[6], attrList[7],
            if (attrList.size > 8) attrList[8] else null, if (attrList.size > 9) attrList[9] else null,
            if (attrList.size > 10) attrList[10] else null, if (attrList.size > 11) attrList[11] else null,
            if (attrList.size > 12) attrList[12] else null, if (attrList.size > 13) attrList[13] else null,
            if (attrList.size > 14) attrList[14] else null, if (attrList.size > 15) attrList[15] else null,
            if (attrList.size > 16) attrList[16] else null, if (attrList.size > 17) attrList[17] else null,
            if (attrList.size > 18) attrList[18] else null, if (attrList.size > 19) attrList[19] else null,
            if (attrList.size > 20) attrList[20] else null
        )
    }
}
