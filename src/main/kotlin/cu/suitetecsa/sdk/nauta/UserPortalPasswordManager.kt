package cu.suitetecsa.sdk.nauta

import cu.suitetecsa.sdk.nauta.exception.NautaChangePasswordException
import cu.suitetecsa.sdk.nauta.scraper.ErrorParser
import cu.suitetecsa.sdk.nauta.scraper.JsoupErrorParser
import cu.suitetecsa.sdk.nauta.util.action.ChangePassword
import cu.suitetecsa.sdk.util.ExceptionHandler

/**
 * Manages the user's password.
 */
class UserPortalPasswordManager private constructor(
    private val sessionManager: UserPortalSessionManager,
    private val errorParser: ErrorParser,
) {

    /**
     * Changes the user's password.
     * @param oldPassword The user's current password.
     * @param newPassword The user's new password.
     * @return Result<Unit> with Unit if the operation was successful.
     */
    fun changePassword(oldPassword: String, newPassword: String) = runCatching {
        val action = ChangePassword(oldPassword = oldPassword, newPassword = newPassword)
        val changePasswordExceptionHandler = ExceptionHandler.Builder(NautaChangePasswordException::class.java).build()

        if (sessionManager.sessionOwner.isNullOrEmpty()) {
            throw changePasswordExceptionHandler
                .handleException("you are not logged in", listOf())
        }

        sessionManager.communicator.performRequest(action.copy(csrf = sessionManager.loadCsrf(action))) {
            errorParser.parseErrors(it.text, "Fail to change password", changePasswordExceptionHandler).getOrThrow()
        }.getOrThrow()
        Unit
    }

    /**
     * Changes the user's email password.
     * @param oldPassword The user's current password.
     * @param newPassword The user's new password.
     * @return Result<Unit> with Unit if the operation was successful.
     */
    fun changeEmailPassword(oldPassword: String, newPassword: String) = runCatching {
        val action = ChangePassword(oldPassword = oldPassword, newPassword = newPassword, changeMail = true)
        val changePasswordExceptionHandler = ExceptionHandler.Builder(NautaChangePasswordException::class.java).build()

        if (sessionManager.sessionOwner.isNullOrEmpty()) {
            throw changePasswordExceptionHandler
                .handleException("you are not logged in", listOf())
        }

        sessionManager.communicator.performRequest(action.copy(csrf = sessionManager.loadCsrf(action))) {
            errorParser.parseErrors(it.text, "Fail to change password", changePasswordExceptionHandler).getOrThrow()
        }.getOrThrow()
        Unit
    }

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
