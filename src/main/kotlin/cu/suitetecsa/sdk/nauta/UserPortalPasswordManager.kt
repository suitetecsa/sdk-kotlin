package cu.suitetecsa.sdk.nauta

import cu.suitetecsa.sdk.nauta.exception.NautaChangePasswordException
import cu.suitetecsa.sdk.nauta.network.UserPortalSession
import cu.suitetecsa.sdk.nauta.scraper.ErrorParser
import cu.suitetecsa.sdk.nauta.scraper.JsoupErrorParser
import cu.suitetecsa.sdk.nauta.scraper.JsoupTokenParser
import cu.suitetecsa.sdk.nauta.scraper.TokenParser
import cu.suitetecsa.sdk.nauta.util.action.ChangePassword
import cu.suitetecsa.sdk.network.Action
import cu.suitetecsa.sdk.network.JsoupPortalCommunicator
import cu.suitetecsa.sdk.network.PortalCommunicator
import cu.suitetecsa.sdk.util.ExceptionHandler

/**
 * Manages the user's password.
 */
class UserPortalPasswordManager private constructor(
    private val communicator: PortalCommunicator,
    private val errorParser: ErrorParser,
    private val tokenParser: TokenParser
) {
    private fun loadCsrfToken(action: Action) =
        communicator.performRequest(action.url) {
            tokenParser.parseCsrfToken(errorParser.parseErrors(it.text, "Fail to load csrf token").getOrThrow())
        }.getOrThrow()

    /**
     * Changes the user's password.
     * @param oldPassword The user's current password.
     * @param newPassword The user's new password.
     * @return Result<Unit> with Unit if the operation was successful.
     */
    fun changePassword(oldPassword: String, newPassword: String) = runCatching {
        val action = ChangePassword(oldPassword = oldPassword, newPassword = newPassword)
        val changePasswordExceptionHandler = ExceptionHandler.Builder(NautaChangePasswordException::class.java).build()

        communicator.performRequest(action.copy(csrf = loadCsrfToken(action))) {
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

        communicator.performRequest(action.copy(csrf = loadCsrfToken(action))) {
            errorParser.parseErrors(it.text, "Fail to change password", changePasswordExceptionHandler).getOrThrow()
        }.getOrThrow()
        Unit
    }

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
