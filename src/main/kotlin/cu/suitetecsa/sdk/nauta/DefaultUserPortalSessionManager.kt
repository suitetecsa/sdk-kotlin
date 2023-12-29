package cu.suitetecsa.sdk.nauta

import cu.suitetecsa.sdk.nauta.scraper.ErrorParser
import cu.suitetecsa.sdk.nauta.scraper.JsoupErrorParser
import cu.suitetecsa.sdk.nauta.scraper.JsoupTokenParser
import cu.suitetecsa.sdk.nauta.scraper.TokenParser
import cu.suitetecsa.sdk.network.Action
import cu.suitetecsa.sdk.network.DefaultSession
import cu.suitetecsa.sdk.network.JsoupPortalCommunicator
import cu.suitetecsa.sdk.network.PortalCommunicator

class DefaultUserPortalSessionManager private constructor(
    override val communicator: PortalCommunicator,
    private val errorParser: ErrorParser,
    private val tokenParser: TokenParser
) : UserPortalSessionManager {
    private var _sessionOwner: String? = null
    private var _isNautaHome: Boolean? = null
    override var sessionOwner: String?
        get() = _sessionOwner
        set(value) {
            _sessionOwner = value
        }
    override var isNautaHome: Boolean?
        get() = _isNautaHome
        set(value) { _isNautaHome = value }

    override fun loadCsrf(action: Action): String =
        communicator.performRequest("https://www.portal.nauta.cu${action.csrfUrl ?: action.url}") {
            tokenParser.parseCsrfToken(errorParser.parseErrors(it.text, "Fail to load csrf token").getOrThrow())
        }.getOrThrow()

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
        fun build() = DefaultUserPortalSessionManager(
            communicator = communicator ?: JsoupPortalCommunicator
                .Builder()
                .withSession(DefaultSession.Builder().build())
                .build(),
            errorParser = errorParser ?: JsoupErrorParser,
            tokenParser = tokenParser ?: JsoupTokenParser
        )
    }
}
