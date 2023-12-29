package cu.suitetecsa.sdk.nauta.rxjava

import cu.suitetecsa.sdk.nauta.DefaultUserPortalSessionManager
import cu.suitetecsa.sdk.nauta.UserPortalSessionManager
import cu.suitetecsa.sdk.nauta.scraper.ErrorParser
import cu.suitetecsa.sdk.nauta.scraper.JsoupErrorParser
import cu.suitetecsa.sdk.nauta.scraper.TokenParser
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class UserPortalBalanceHandler private constructor(
    private val sessionManager: UserPortalSessionManager,
    private val errorParser: ErrorParser,
) {
    private val balanceHandler = cu.suitetecsa.sdk.nauta.UserPortalBalanceHandler.Builder()
        .withSessionManager(sessionManager)
        .withErrorParser(errorParser)
        .build()

    fun topUpBalance(rechargeCode: String) =
        Observable.fromCallable { balanceHandler.topUpBalance(rechargeCode).getOrThrow() }
            .subscribeOn(Schedulers.io())

    fun transferFunds(amount: Float, password: String, destinationAccount: String? = null) =
        Observable.fromCallable { balanceHandler.transferFunds(amount, password, destinationAccount).getOrThrow() }
            .subscribeOn(Schedulers.io())

    /**
     * Builder for the UserPortalBalanceHandler.
     */
    class Builder {
        private var sessionManager: UserPortalSessionManager? = null
        private var errorParser: ErrorParser? = null
        private var tokenParser: TokenParser? = null

        /**
         * Sets the communicator to use.
         * @param sessionManager The communicator to use.
         * @return Builder
         */
        fun setCommunicator(sessionManager: UserPortalSessionManager) = apply { this.sessionManager = sessionManager }

        /**
         * Sets the error parser to use.
         * @param errorParser The error parser to use.
         * @return Builder
         */
        fun setErrorParser(errorParser: ErrorParser) = apply { this.errorParser = errorParser }

        /**
         * Sets the token parser to use.
         * @param tokenParser The token parser to use.
         * @return Builder
         */
        fun setTokenParser(tokenParser: TokenParser) = apply { this.tokenParser = tokenParser }

        /**
         * Builds the UserPortalBalanceHandler.
         * @return UserPortalBalanceHandler
         */
        fun build() = UserPortalBalanceHandler(
            sessionManager = sessionManager ?: DefaultUserPortalSessionManager
                .Builder()
                .build(),
            errorParser = errorParser ?: JsoupErrorParser,
        )
    }
}
