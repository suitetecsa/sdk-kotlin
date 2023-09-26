package cu.suitetecsa.sdk.nauta.util

import cu.suitetecsa.sdk.nauta.exception.NotLoggedInException
import cu.suitetecsa.sdk.util.ExceptionHandler
import org.jsoup.nodes.Document

internal fun Document.throwExceptionOnFailure(
    message: String,
    portalManager: PortalManager,
    exceptionHandler: ExceptionHandler? = null
) = HtmlErrorParser.whenPortalManager(portalManager).parseError(this)?.let {
    val errors = if (it.startsWith("Se han detectado algunos errores.")) {
        select("li[class='sub-message']").map { subMessage -> subMessage.text() }
    } else {
        listOf(it)
    }
    if (errors.size == 1 && errors.first() in notLoggedInErrors) {
        throw ExceptionHandler.Builder(NotLoggedInException::class.java)
            .build()
            .handleException(message, errors)
    }
    exceptionHandler?.let { handler ->
        throw handler.handleException(message, errors)
    } ?: throw ExceptionHandler.Builder().build().handleException(message, errors)
}
