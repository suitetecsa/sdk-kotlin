package io.github.suitetecsa.sdk.access.scraper

import io.github.suitetecsa.sdk.access.network.HttpResponse
import io.github.suitetecsa.sdk.access.utils.ExceptionHandler
import io.github.suitetecsa.sdk.exception.NotLoggedInException

interface ErrorParser {
    @Throws(NotLoggedInException::class)
    fun <T : Exception> throwExceptionOnFailure(
        httpResponse: HttpResponse,
        message: String,
        exceptionHandler: ExceptionHandler<T>
    ): HttpResponse

    class Builder {
        fun build(): ErrorParser {
            return ErrorParserImpl()
        }
    }
}
