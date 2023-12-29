package cu.suitetecsa.sdk.nauta.rxjava

import cu.suitetecsa.sdk.nauta.DefaultUserPortalSessionManager
import cu.suitetecsa.sdk.nauta.UserPortalSessionManager
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
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import cu.suitetecsa.sdk.nauta.UserPortalActionsProvider as ActionsProvider

@Suppress("TooManyFunctions")
class UserPortalActionsProvider private constructor(
    private val sessionManager: UserPortalSessionManager,
    private val errorParser: ErrorParser,
    private val actionsParser: ActionsParser,
    private val summaryParser: ActionsSummaryParser
) {
    private val actionsProvider = ActionsProvider.Builder()
        .withSessionManager(sessionManager)
        .withErrorParser(errorParser)
        .withActionsParser(actionsParser)
        .withSummaryParser(summaryParser)
        .build()

    fun getConnectionsSummary(year: Int, month: Int) =
        Observable.fromCallable { actionsProvider.getConnectionsSummary(year, month).getOrThrow() }
            .subscribeOn(Schedulers.io())

    fun getConnections(summary: ConnectionsSummary, pageTo: Int = 0, reversed: Boolean = false) =
        Observable.fromCallable { actionsProvider.getConnections(summary, pageTo, reversed).getOrThrow() }
            .subscribeOn(Schedulers.io())

    fun getConnections(year: Int, month: Int, pageTo: Int = 0, reversed: Boolean = false) =
        Observable.fromCallable { actionsProvider.getConnections(year, month, pageTo, reversed).getOrThrow() }
            .subscribeOn(Schedulers.io())

    fun getRechargesSummary(year: Int, month: Int) =
        Observable.fromCallable { actionsProvider.getRechargesSummary(year, month).getOrThrow() }
            .subscribeOn(Schedulers.io())

    fun getRecharges(summary: RechargesSummary, pageTo: Int = 0, reversed: Boolean = false) =
        Observable.fromCallable { actionsProvider.getRecharges(summary, pageTo, reversed).getOrThrow() }
            .subscribeOn(Schedulers.io())

    fun getRecharges(year: Int, month: Int, pageTo: Int = 0, reversed: Boolean = false) =
        Observable.fromCallable { actionsProvider.getRecharges(year, month, pageTo, reversed).getOrThrow() }
            .subscribeOn(Schedulers.io())

    fun getTransfersSummary(year: Int, month: Int) =
        Observable.fromCallable { actionsProvider.getTransfersSummary(year, month).getOrThrow() }
            .subscribeOn(Schedulers.io())

    fun getTransfers(summary: TransfersSummary, pageTo: Int = 0, reversed: Boolean = false) =
        Observable.fromCallable { actionsProvider.getTransfers(summary, pageTo, reversed).getOrThrow() }
            .subscribeOn(Schedulers.io())

    fun getTransfers(year: Int, month: Int, pageTo: Int = 0, reversed: Boolean = false) =
        Observable.fromCallable { actionsProvider.getTransfers(year, month, pageTo, reversed).getOrThrow() }
            .subscribeOn(Schedulers.io())

    fun getQuotesPaidSummary(year: Int, month: Int) =
        Observable.fromCallable { actionsProvider.getQuotesPaidSummary(year, month).getOrThrow() }
            .subscribeOn(Schedulers.io())

    fun getQuotesPaid(summary: QuotesPaidSummary, pageTo: Int = 0, reversed: Boolean = false) =
        Observable.fromCallable { actionsProvider.getQuotesPaid(summary, pageTo, reversed).getOrThrow() }
            .subscribeOn(Schedulers.io())

    fun getQuotesPaid(year: Int, month: Int, pageTo: Int = 0, reversed: Boolean = false) =
        Observable.fromCallable { actionsProvider.getQuotesPaid(year, month, pageTo, reversed).getOrThrow() }
            .subscribeOn(Schedulers.io())

    class Builder {
        private var sessionManager: UserPortalSessionManager? = null
        private var errorParser: ErrorParser? = null
        private var actionsParser: ActionsParser? = null
        private var summaryParser: ActionsSummaryParser? = null

        fun withCommunicator(sessionManager: UserPortalSessionManager) = apply { this.sessionManager = sessionManager }
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
