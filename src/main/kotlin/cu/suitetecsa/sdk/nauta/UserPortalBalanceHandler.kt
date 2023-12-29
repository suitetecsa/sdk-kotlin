package cu.suitetecsa.sdk.nauta

import cu.suitetecsa.sdk.nauta.exception.TopUpBalanceException
import cu.suitetecsa.sdk.nauta.exception.TransferFundsException
import cu.suitetecsa.sdk.nauta.scraper.ErrorParser
import cu.suitetecsa.sdk.nauta.scraper.JsoupErrorParser
import cu.suitetecsa.sdk.nauta.util.action.TopUpBalance
import cu.suitetecsa.sdk.nauta.util.action.TransferFunds
import cu.suitetecsa.sdk.network.HttpMethod
import cu.suitetecsa.sdk.util.ExceptionHandler

/**
 * Handles the balance related actions on the user portal.
 */
class UserPortalBalanceHandler private constructor(
    private val sessionManager: UserPortalSessionManager,
    private val errorParser: ErrorParser,
) {

    /**
     * Top up the balance with the given recharge code.
     * @param rechargeCode The recharge code to use.
     * @return Result<Unit>
     */
    fun topUpBalance(rechargeCode: String): Result<Unit> = runCatching {
        val action = TopUpBalance(rechargeCode = rechargeCode, method = HttpMethod.GET)
        val topUpExceptionHandle = ExceptionHandler.Builder(TopUpBalanceException::class.java).build()

        if (sessionManager.sessionOwner.isNullOrEmpty()) {
            throw topUpExceptionHandle
                .handleException("you are not logged in", listOf())
        }

        sessionManager.communicator.performRequest(
            action.copy(csrf = sessionManager.loadCsrf(action), method = HttpMethod.POST)
        ) {
            errorParser.parseErrors(it.text, "Fail to top up balance", topUpExceptionHandle).getOrThrow()
        }.getOrThrow()
    }

    /**
     * Transfer funds to another account or pay a quote of the Nauta Home service if no destination account is given.
     * @param amount The amount to transfer.
     * @param password The user's password.
     * @param destinationAccount The account to transfer to.
     * @return Result<Unit>
     */
    fun transferFunds(amount: Float, password: String, destinationAccount: String? = null): Result<Unit> = runCatching {
        val action = TransferFunds(
            amount = amount,
            destinationAccount = destinationAccount,
            password = password,
            method = HttpMethod.GET
        )
        val transferFundsExceptionHandle = ExceptionHandler.Builder(TransferFundsException::class.java).build()
        sessionManager.isNautaHome?.let {
            if (!it && destinationAccount == null) {
                throw transferFundsExceptionHandle
                    .handleException("this account is not Nauta Home", listOf())
            }
        } ?: run {
            throw transferFundsExceptionHandle.handleException("you are not logged in", listOf())
        }

        sessionManager.communicator.performRequest(
            action.copy(csrf = sessionManager.loadCsrf(action), method = HttpMethod.POST)
        ) {
            errorParser.parseErrors(it.text, "Fail to transfer funds", transferFundsExceptionHandle).getOrThrow()
        }.getOrThrow()
    }

    /**
     * Builder for the UserPortalBalanceHandler.
     */
    class Builder {
        private var sessionManager: UserPortalSessionManager? = null
        private var errorParser: ErrorParser? = null

        /**
         * Sets the communicator to use.
         * @param sessionManager The communicator to use.
         * @return Builder
         */
        fun withSessionManager(sessionManager: UserPortalSessionManager) =
            apply { this.sessionManager = sessionManager }

        /**
         * Sets the error parser to use.
         * @param errorParser The error parser to use.
         * @return Builder
         */
        fun withErrorParser(errorParser: ErrorParser) = apply { this.errorParser = errorParser }

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
