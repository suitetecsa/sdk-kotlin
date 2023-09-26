package cu.suitetecsa.sdk.nauta.scraper

import cu.suitetecsa.sdk.nauta.exception.LoadInfoException
import cu.suitetecsa.sdk.nauta.model.Connection
import cu.suitetecsa.sdk.nauta.model.QuotePaid
import cu.suitetecsa.sdk.nauta.model.Recharge
import cu.suitetecsa.sdk.nauta.model.Transfer
import cu.suitetecsa.sdk.nauta.util.PortalManager
import cu.suitetecsa.sdk.nauta.util.component6
import cu.suitetecsa.sdk.nauta.util.throwExceptionOnFailure
import cu.suitetecsa.sdk.nauta.util.toBytes
import cu.suitetecsa.sdk.nauta.util.toDateTime
import cu.suitetecsa.sdk.nauta.util.toPriceFloat
import cu.suitetecsa.sdk.nauta.util.toSeconds
import cu.suitetecsa.sdk.util.ExceptionHandler
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

internal object JsoupActionsParser : ActionsParser {
    private val loadInfoExceptionHandler = ExceptionHandler.Builder(LoadInfoException::class.java).build()
    private fun <T> parseList(html: String, constructor: (Element) -> T) = Jsoup.parse(html).let { parsedHtml ->
        parsedHtml.throwExceptionOnFailure("Fail to get action list", PortalManager.User, loadInfoExceptionHandler)
        parsedHtml.selectFirst(".responsive-table > tbody")
            ?.let { body ->
                body.select("tr").map { constructor(it) }
            } ?: emptyList()
    }

    override fun parseConnections(html: String): List<Connection> =
        parseList(html, ::parseConnection)

    override fun parseRecharges(html: String): List<Recharge> =
        parseList(html, ::parseRecharge)

    override fun parseTransfers(html: String): List<Transfer> =
        parseList(html, ::parseTransfer)

    override fun parseQuotesPaid(html: String): List<QuotePaid> =
        parseList(html, ::parseQuotePaid)

    @Suppress("DestructuringDeclarationWithTooManyEntries")
    private fun parseConnection(element: Element): Connection {
        val (startSessionTag, endSessionTag, durationTag, uploadedTag, downloadedTag, importTag) = element.select("td")
        return Connection(
            startSessionTag.text().trim().toDateTime(),
            endSessionTag.text().trim().toDateTime(),
            durationTag.text().trim().toSeconds(),
            uploadedTag.text().trim().toBytes(),
            downloadedTag.text().trim().toBytes(),
            importTag.text().trim().toPriceFloat()
        )
    }

    @Suppress("DestructuringDeclarationWithTooManyEntries")
    private fun parseRecharge(element: Element): Recharge {
        val (dateTag, importTag, channelTag, typeTag) = element.select("td")
        return Recharge(
            dateTag.text().trim().toDateTime(),
            importTag.text().trim().toPriceFloat(),
            channelTag.text().trim(),
            typeTag.text().trim()
        )
    }

    private fun parseTransfer(element: Element): Transfer {
        val (dateTag, importTag, destinyAccountTag) = element.select("td")
        return Transfer(
            dateTag.text().trim().toDateTime(),
            importTag.text().trim().toPriceFloat(),
            destinyAccountTag.text().trim()
        )
    }

    @Suppress("DestructuringDeclarationWithTooManyEntries")
    private fun parseQuotePaid(element: Element): QuotePaid {
        val (dateTag, importTag, channelTag, typeTag, officeTag) = element.select("td")
        return QuotePaid(
            dateTag.text().trim().toDateTime(),
            importTag.text().trim().toPriceFloat(),
            channelTag.text().trim(),
            typeTag.text().trim(),
            officeTag.text().trim()
        )
    }
}
