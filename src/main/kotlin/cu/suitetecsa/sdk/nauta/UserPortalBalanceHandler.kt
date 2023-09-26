package cu.suitetecsa.sdk.nauta

import cu.suitetecsa.sdk.nauta.exception.TopUpBalanceException
import cu.suitetecsa.sdk.nauta.exception.TransferFundsException
import cu.suitetecsa.sdk.nauta.network.UserPortalSession
import cu.suitetecsa.sdk.nauta.scraper.ErrorParser
import cu.suitetecsa.sdk.nauta.scraper.JsoupErrorParser
import cu.suitetecsa.sdk.nauta.scraper.JsoupTokenParser
import cu.suitetecsa.sdk.nauta.scraper.TokenParser
import cu.suitetecsa.sdk.nauta.util.action.TopUpBalance
import cu.suitetecsa.sdk.nauta.util.action.TransferFunds
import cu.suitetecsa.sdk.network.Action
import cu.suitetecsa.sdk.network.HttpMethod
import cu.suitetecsa.sdk.network.JsoupPortalCommunicator
import cu.suitetecsa.sdk.network.PortalCommunicator
import cu.suitetecsa.sdk.util.ExceptionHandler

/**
 * Handles the balance related actions on the user portal.
 */
class UserPortalBalanceHandler private constructor(
    private val communicator: PortalCommunicator,
    private val errorParser: ErrorParser,
    private val tokenParser: TokenParser
) {
    private fun loadCsrfToken(action: Action) =
        communicator.performAction("https://www.portal.nauta.cu${action.csrfUrl ?: action.url}") {
            tokenParser.parseCsrfToken(errorParser.parseErrors(it.text, "Fail to load csrf token").getOrThrow())
        }.getOrThrow()

    /**
     * Top up the balance with the given recharge code.
     * @param rechargeCode The recharge code to use.
     * @return Result<Unit>
     */
    fun topUpBalance(rechargeCode: String) = runCatching {
        val action = TopUpBalance(rechargeCode = rechargeCode, method = HttpMethod.GET)
        val topUpExceptionHandle = ExceptionHandler.Builder(TopUpBalanceException::class.java).build()

        communicator.performAction(action.copy(csrf = loadCsrfToken(action), method = HttpMethod.POST)) {
            errorParser.parseErrors(it.text, "Fail to top up balance", topUpExceptionHandle).getOrThrow()
        }.getOrThrow()
        Unit
    }

    /**
     * Transfer funds to another account or pay a quote of the Nauta Home service if no destination account is given.
     * @param amount The amount to transfer.
     * @param password The user's password.
     * @param destinationAccount The account to transfer to.
     * @return Result<Unit>
     */
    fun transferFunds(amount: Float, password: String, destinationAccount: String? = null) = runCatching {
        val action = TransferFunds(
            amount = amount,
            destinationAccount = destinationAccount,
            password = password,
            method = HttpMethod.GET
        )
        val transferFundsExceptionHandle = ExceptionHandler.Builder(TransferFundsException::class.java).build()

        communicator.performAction(action.copy(csrf = loadCsrfToken(action), method = HttpMethod.POST)) {
            errorParser.parseErrors(it.text, "Fail to transfer funds", transferFundsExceptionHandle).getOrThrow()
        }.getOrThrow()
        Unit
    }

    /**
     * Builder for the UserPortalBalanceHandler.
     */
    class Builder {
        private var communicator: PortalCommunicator? = null
        private var errorParser: ErrorParser? = null
        private var tokenParser: TokenParser? = null

        /**
         * Sets the communicator to use.
         * @param communicator The communicator to use.
         * @return Builder
         */
        fun withCommunicator(communicator: PortalCommunicator) = apply { this.communicator = communicator }

        /**
         * Sets the error parser to use.
         * @param errorParser The error parser to use.
         * @return Builder
         */
        fun withErrorParser(errorParser: ErrorParser) = apply { this.errorParser = errorParser }

        /**
         * Sets the token parser to use.
         * @param tokenParser The token parser to use.
         * @return Builder
         */
        fun withTokenParser(tokenParser: TokenParser) = apply { this.tokenParser = tokenParser }

        /**
         * Builds the UserPortalBalanceHandler.
         * @return UserPortalBalanceHandler
         */
        fun build() = UserPortalBalanceHandler(
            communicator = communicator ?: JsoupPortalCommunicator
                .Builder()
                .withSession(UserPortalSession)
                .build(),
            errorParser = errorParser ?: JsoupErrorParser,
            tokenParser = tokenParser ?: JsoupTokenParser
        )
    }
}
