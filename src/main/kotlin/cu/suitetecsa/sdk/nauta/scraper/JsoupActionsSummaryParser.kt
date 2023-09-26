package cu.suitetecsa.sdk.nauta.scraper

import cu.suitetecsa.sdk.nauta.exception.LoadInfoException
import cu.suitetecsa.sdk.nauta.model.ConnectionsSummary
import cu.suitetecsa.sdk.nauta.model.QuotesPaidSummary
import cu.suitetecsa.sdk.nauta.model.RechargesSummary
import cu.suitetecsa.sdk.nauta.model.TransfersSummary
import cu.suitetecsa.sdk.nauta.util.PortalManager
import cu.suitetecsa.sdk.nauta.util.component6
import cu.suitetecsa.sdk.nauta.util.throwExceptionOnFailure
import cu.suitetecsa.sdk.nauta.util.toBytes
import cu.suitetecsa.sdk.nauta.util.toPriceFloat
import cu.suitetecsa.sdk.nauta.util.toSeconds
import cu.suitetecsa.sdk.util.ExceptionHandler
import org.jsoup.Jsoup

internal object JsoupActionsSummaryParser : ActionsSummaryParser {
    private val loadInfoExceptionHandler = ExceptionHandler.Builder(LoadInfoException::class.java).build()

    @Suppress("DestructuringDeclarationWithTooManyEntries")
    override fun parseConnectionsSummary(html: String) = Jsoup.parse(html).let { htmlParsed ->
        htmlParsed.throwExceptionOnFailure(
            "Fail to get connections summary",
            PortalManager.User,
            loadInfoExceptionHandler
        )

        htmlParsed.selectFirst("#content")!!.select(".card-content")
            .let { (connections, totalTime, totalImport, uploader, downloader, totalTraffic) ->
                ConnectionsSummary(
                    connections.selectFirst("input[name=count]")!!.attr("value").toInt(),
                    connections.selectFirst("input[name=year_month_selected]")!!.attr("value"),
                    totalTime.selectFirst(".card-stats-number")!!.text().trim().toSeconds(),
                    totalImport.selectFirst(".card-stats-number")!!.text().trim().toPriceFloat(),
                    uploader.selectFirst(".card-stats-number")!!.text().trim().toBytes(),
                    downloader.selectFirst(".card-stats-number")!!.text().trim().toBytes(),
                    totalTraffic.selectFirst(".card-stats-number")!!.text().trim().toBytes()
                )
            }
    }

    private fun parseSummary(html: String) = Jsoup.parse(html).let { parsedHtml ->
        parsedHtml.throwExceptionOnFailure("Fail to get summary", PortalManager.User, loadInfoExceptionHandler)

        parsedHtml.selectFirst("#content")!!.select(".card-content").let { (data, totalImport) ->
            Triple(
                data.selectFirst("input[name=count]")?.attr("value")?.toIntOrNull() ?: 0,
                data.selectFirst("input[name=year_month_selected]")?.attr("value") ?: "",
                totalImport.selectFirst(".card-stats-number")?.text()?.trim()?.toPriceFloat() ?: 0f
            )
        }
    }

    override fun parseRechargesSummary(html: String) = parseSummary(html)
        .let { (count, yearMonthSelected, totalImport) ->
            RechargesSummary(count, yearMonthSelected, totalImport)
        }

    override fun parseTransfersSummary(html: String) = parseSummary(html)
        .let { (count, yearMonthSelected, totalImport) ->
            TransfersSummary(count, yearMonthSelected, totalImport)
        }

    override fun parseQuotesPaidSummary(html: String) = parseSummary(html)
        .let { (count, yearMonthSelected, totalImport) ->
            QuotesPaidSummary(count, yearMonthSelected, totalImport)
        }
}
