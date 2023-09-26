package cu.suitetecsa.sdk.nauta.rxjava

import cu.suitetecsa.sdk.nauta.network.UserPortalSession
import cu.suitetecsa.sdk.nauta.scraper.ErrorParser
import cu.suitetecsa.sdk.nauta.scraper.JsoupErrorParser
import cu.suitetecsa.sdk.nauta.scraper.JsoupTokenParser
import cu.suitetecsa.sdk.nauta.scraper.TokenParser
import cu.suitetecsa.sdk.network.JsoupPortalCommunicator
import cu.suitetecsa.sdk.network.PortalCommunicator
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import cu.suitetecsa.sdk.nauta.UserPortalPasswordManager as PasswordManager

class UserPortalPasswordManager private constructor(
    private val communicator: PortalCommunicator,
    private val errorParser: ErrorParser,
    private val tokenParser: TokenParser
) {
    private val passwordManager = PasswordManager.Builder()
        .withCommunicator(communicator)
        .withErrorParser(errorParser)
        .withTokenParser(tokenParser)
        .build()

    fun changePassword(oldPassword: String, newPassword: String) =
        Observable.fromCallable { passwordManager.changePassword(oldPassword, newPassword).getOrThrow() }
            .subscribeOn(Schedulers.io())

    fun changeEmailPassword(oldPassword: String, newPassword: String) =
        Observable.fromCallable { passwordManager.changeEmailPassword(oldPassword, newPassword).getOrThrow() }
            .subscribeOn(Schedulers.io())

    class Builder {
        private var communicator: PortalCommunicator? = null
        private var errorParser: ErrorParser? = null
        private var tokenParser: TokenParser? = null

        fun withCommunicator(communicator: PortalCommunicator) = apply { this.communicator = communicator }
        fun withErrorParser(errorParser: ErrorParser) = apply { this.errorParser = errorParser }
        fun withTokenParser(tokenParser: TokenParser) = apply { this.tokenParser = tokenParser }

        fun build() = UserPortalPasswordManager(
            communicator = communicator ?: JsoupPortalCommunicator
                .Builder()
                .withSession(UserPortalSession)
                .build(),
            errorParser = errorParser ?: JsoupErrorParser,
            tokenParser = tokenParser ?: JsoupTokenParser
        )
    }
}
