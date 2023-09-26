package cu.suitetecsa.sdk.nauta.scraper

import org.jsoup.Jsoup

internal object JsoupTokenParser : TokenParser {
    override fun parseCsrfToken(html: String) =
        Jsoup.parse(html).selectFirst("input[name=csrf]")?.attr("value").orEmpty()
}
