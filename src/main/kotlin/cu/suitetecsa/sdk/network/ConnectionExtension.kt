package cu.suitetecsa.sdk.network

import cu.suitetecsa.sdk.util.ExceptionFactory
import cu.suitetecsa.sdk.util.ExceptionHandler
import org.jsoup.Connection

private const val STATUS_OK_RANGE_START = 200
private const val STATUS_OK_RANGE_END = 299
private val STATUS_OK_RANGE = STATUS_OK_RANGE_START..STATUS_OK_RANGE_END
private const val STATUS_REDIRECT_RANGE_START = 300
private const val STATUS_REDIRECT_RANGE_END = 399
private val STATUS_REDIRECT_RANGE = STATUS_REDIRECT_RANGE_START..STATUS_REDIRECT_RANGE_END

/**
 * This code snippet is a function called throwExceptionOnFailure that is an
 * extension function of the Connection.Response class. It throws an exception
 * if the response indicates a failure based on the status code
 *
 * @param message The error message to include in the exception.
 * @param exceptionFactory The factory to create the exception (optional).
 */
internal fun Connection.Response.throwExceptionOnFailure(
    message: String,
    exceptionFactory: ExceptionFactory? = null
) {
    val statusCode = this.statusCode()
    if (statusCode !in STATUS_OK_RANGE && !(statusCode in STATUS_REDIRECT_RANGE && this.hasHeader("Location"))) {
        throw ExceptionHandler
            .Builder()
            .let { exceptionFactory?.let { excFactory -> it.setExceptionFactory(excFactory) } ?: it }
            .build()
            .handleException(message, listOf(this.statusMessage()))
    }
}
