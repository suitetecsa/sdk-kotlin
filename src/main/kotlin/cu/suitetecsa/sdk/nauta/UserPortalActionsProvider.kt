package cu.suitetecsa.sdk.nauta

import cu.suitetecsa.sdk.nauta.exception.NautaAttributeException
import cu.suitetecsa.sdk.nauta.exception.NautaGetInfoException
import cu.suitetecsa.sdk.nauta.model.ConnectionsSummary
import cu.suitetecsa.sdk.nauta.model.QuotesPaidSummary
import cu.suitetecsa.sdk.nauta.model.RechargesSummary
import cu.suitetecsa.sdk.nauta.model.TransfersSummary
import cu.suitetecsa.sdk.nauta.scraper.ActionsParser
import cu.suitetecsa.sdk.nauta.scraper.ActionsSummaryParser
import cu.suitetecsa.sdk.nauta.scraper.ErrorParser
import cu.suitetecsa.sdk.nauta.scraper.JsoupActionsParser
import cu.suitetecsa.sdk.nauta.scraper.JsoupActionsSummaryParser
import cu.suitetecsa.sdk.nauta.scraper.JsoupErrorParser
import cu.suitetecsa.sdk.nauta.util.action.GetActions
import cu.suitetecsa.sdk.nauta.util.action.GetSummary
import cu.suitetecsa.sdk.nauta.util.pagesCount
import cu.suitetecsa.sdk.network.Action
import cu.suitetecsa.sdk.network.ActionType
import cu.suitetecsa.sdk.network.HttpResponse
import cu.suitetecsa.sdk.util.ExceptionHandler

@Suppress("TooManyFunctions")
class UserPortalActionsProvider private constructor(
    private val sessionManager: UserPortalSessionManager,
    private val errorParser: ErrorParser,
    private val actionsParser: ActionsParser,
    private val summaryParser: ActionsSummaryParser
) {
    private val getInfoExceptionHandler = ExceptionHandler.Builder(NautaGetInfoException::class.java).build()
    fun getConnectionsSummary(year: Int, month: Int) = runCatching {
        val action = GetSummary(year = year, month = month, type = ActionType.Connections)
        val csrf = sessionManager.loadCsrf(action)

        sessionManager.communicator.performRequest(action.copy(csrf = csrf)) {
            summaryParser.parseConnectionsSummary(
                errorParser.parseErrors(
                    it.text,
                    "Fail to load connections summary",
                    getInfoExceptionHandler
                ).getOrThrow()
            )
        }.getOrThrow()
    }

    fun getConnections(
        summary: ConnectionsSummary,
        pageTo: Int = 0,
        reversed: Boolean = false
    ) = runCatching {
        val action = GetActions(
            summary.count,
            summary.yearMonthSelected,
            summary.pagesCount,
            reversed,
            ActionType.Connections
        )
        if (pageTo > summary.pagesCount) throw NautaAttributeException("Page to is greater than pages count")
        if (summary.count != 0) {
            getActions(action, pageTo) {
                actionsParser.parseConnections(
                    errorParser
                        .parseErrors(it.text, "Fail to load connections").getOrThrow()
                )
            }.getOrThrow()
        } else {
            emptyList()
        }
    }

    fun getConnections(year: Int, month: Int, pageTo: Int = 0, reversed: Boolean = false) =
        getConnectionsSummary(year, month).mapCatching { summary ->
            getConnections(summary, pageTo, reversed).getOrThrow()
        }

    fun getRechargesSummary(year: Int, month: Int) = runCatching {
        val action = GetSummary(year = year, month = month, type = ActionType.Recharges)
        val csrf = sessionManager.loadCsrf(action)

        sessionManager.communicator.performRequest(action.copy(csrf = csrf)) {
            summaryParser.parseRechargesSummary(
                errorParser.parseErrors(
                    it.text,
                    "Fail to load recharges summary",
                    getInfoExceptionHandler
                ).getOrThrow()
            )
        }.getOrThrow()
    }

    fun getRecharges(
        summary: RechargesSummary,
        pageTo: Int = 0,
        reversed: Boolean = false
    ) = runCatching {
        val action = GetActions(
            summary.count,
            summary.yearMonthSelected,
            summary.pagesCount,
            reversed,
            ActionType.Recharges
        )
        if (pageTo > summary.pagesCount) throw NautaAttributeException("Page to is greater than pages count")
        if (summary.count != 0) {
            getActions(action, pageTo) {
                actionsParser.parseRecharges(
                    errorParser
                        .parseErrors(it.text, "Fail to load recharges").getOrThrow()
                )
            }.getOrThrow()
        } else {
            emptyList()
        }
    }

    fun getRecharges(year: Int, month: Int, pageTo: Int = 0, reversed: Boolean = false) =
        getRechargesSummary(year, month).mapCatching { summary ->
            getRecharges(summary, pageTo, reversed).getOrThrow()
        }

    fun getTransfersSummary(year: Int, month: Int) = runCatching {
        val action = GetSummary(year = year, month = month, type = ActionType.Transfers)
        val csrf = sessionManager.loadCsrf(action)

        sessionManager.communicator.performRequest(action.copy(csrf = csrf)) {
            summaryParser.parseTransfersSummary(
                errorParser.parseErrors(
                    it.text,
                    "Fail to load transfers summary",
                    getInfoExceptionHandler
                ).getOrThrow()
            )
        }.getOrThrow()
    }

    fun getTransfers(
        summary: TransfersSummary,
        pageTo: Int = 0,
        reversed: Boolean = false
    ) = runCatching {
        val action = GetActions(
            summary.count,
            summary.yearMonthSelected,
            summary.pagesCount,
            reversed,
            ActionType.Transfers
        )
        if (pageTo > summary.pagesCount) throw NautaAttributeException("Page to is greater than pages count")
        if (summary.count != 0) {
            getActions(action, pageTo) {
                actionsParser.parseTransfers(
                    errorParser
                        .parseErrors(it.text, "Fail to load transfers").getOrThrow()
                )
            }.getOrThrow()
        } else {
            emptyList()
        }
    }

    fun getTransfers(year: Int, month: Int, pageTo: Int = 0, reversed: Boolean = false) =
        getTransfersSummary(year, month).mapCatching { summary ->
            getTransfers(summary, pageTo, reversed).getOrThrow()
        }

    fun getQuotesPaidSummary(year: Int, month: Int) = runCatching {
        val action = GetSummary(year = year, month = month, type = ActionType.QuotesPaid)
        val csrf = sessionManager.loadCsrf(action)

        sessionManager.communicator.performRequest(action.copy(csrf = csrf)) {
            summaryParser.parseQuotesPaidSummary(
                errorParser.parseErrors(
                    it.text,
                    "Fail to load quotes paid summary",
                    getInfoExceptionHandler
                ).getOrThrow()
            )
        }.getOrThrow()
    }

    fun getQuotesPaid(summary: QuotesPaidSummary, pageTo: Int = 0, reversed: Boolean = false) =
        runCatching {
            val action = GetActions(
                summary.count,
                summary.yearMonthSelected,
                summary.pagesCount,
                reversed,
                ActionType.QuotesPaid
            )
            if (pageTo > summary.pagesCount) throw NautaAttributeException("Page to is greater than pages count")
            if (summary.count != 0) {
                getActions(action, pageTo) {
                    actionsParser.parseQuotesPaid(
                        errorParser
                            .parseErrors(it.text, "Fail to load quotes paid").getOrThrow()
                    )
                }.getOrThrow()
            } else {
                emptyList()
            }
        }

    fun getQuotesPaid(year: Int, month: Int, pageTo: Int = 0, reversed: Boolean = false) =
        getQuotesPaidSummary(year, month).mapCatching { summary ->
            getQuotesPaid(summary, pageTo, reversed).getOrThrow()
        }

    private fun <T> getActions(action: Action, pageTo: Int, transform: (HttpResponse) -> List<T>) =
        runCatching {
            if (pageTo != 0) {
                sessionManager.communicator
                    .performRequest("${action.url}${action.yearMonthSelected}/${action.count}/$pageTo") {
                        transform(it)
                    }.getOrThrow()
            } else {
                val list = mutableListOf<T>()
                repeat(action.pagesCount) { page ->
                    sessionManager.communicator.performRequest(
                        "${action.url}${action.yearMonthSelected}/" +
                            "${action.count}${if (page > 0) "/${page + 1}" else ""}"
                    ) {
                        list.addAll(transform(it))
                    }
                }
                list
            }
        }

    class Builder {
        private var sessionManager: UserPortalSessionManager? = null
        private var errorParser: ErrorParser? = null
        private var actionsParser: ActionsParser? = null
        private var summaryParser: ActionsSummaryParser? = null

        fun withSessionManager(sessionManager: UserPortalSessionManager) =
            apply { this.sessionManager = sessionManager }
        fun withErrorParser(errorParser: ErrorParser) = apply { this.errorParser = errorParser }
        fun withActionsParser(actionsParser: ActionsParser) = apply { this.actionsParser = actionsParser }
        fun withSummaryParser(summaryParser: ActionsSummaryParser) = apply { this.summaryParser = summaryParser }
        fun build() = UserPortalActionsProvider(
            sessionManager = sessionManager ?: DefaultUserPortalSessionManager
                .Builder()
                .build(),
            errorParser = errorParser ?: JsoupErrorParser,
            actionsParser = actionsParser ?: JsoupActionsParser,
            summaryParser = summaryParser ?: JsoupActionsSummaryParser
        )
    }
}
