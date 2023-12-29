package cu.suitetecsa.sdk.nauta.rxjava

import cu.suitetecsa.sdk.nauta.DefaultUserPortalSessionManager
import cu.suitetecsa.sdk.nauta.UserPortalSessionManager
import cu.suitetecsa.sdk.nauta.scraper.ErrorParser
import cu.suitetecsa.sdk.nauta.scraper.JsoupErrorParser
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import cu.suitetecsa.sdk.nauta.UserPortalPasswordManager as PasswordManager

class UserPortalPasswordManager private constructor(
    sessionManager: UserPortalSessionManager,
    private val errorParser: ErrorParser,
) {
    private val passwordManager = PasswordManager.Builder()
        .withSessionManager(sessionManager)
        .withErrorParser(errorParser)
        .build()

    fun changePassword(oldPassword: String, newPassword: String) =
        Observable.fromCallable { passwordManager.changePassword(oldPassword, newPassword).getOrThrow() }
            .subscribeOn(Schedulers.io())

    fun changeEmailPassword(oldPassword: String, newPassword: String) =
        Observable.fromCallable { passwordManager.changeEmailPassword(oldPassword, newPassword).getOrThrow() }
            .subscribeOn(Schedulers.io())

    class Builder {
        private var sessionManager: UserPortalSessionManager? = null
        private var errorParser: ErrorParser? = null

        fun withSessionManager(sessionManager: UserPortalSessionManager) =
            apply { this.sessionManager = sessionManager }
        fun withErrorParser(errorParser: ErrorParser) = apply { this.errorParser = errorParser }

        fun build() = UserPortalPasswordManager(
            sessionManager = sessionManager ?: DefaultUserPortalSessionManager
                .Builder()
                .build(),
            errorParser = errorParser ?: JsoupErrorParser,
        )
    }
}
