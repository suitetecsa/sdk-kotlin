package io.github.suitetecsa.sdk.access.scraper

import io.github.suitetecsa.sdk.access.network.HttpResponse
import io.github.suitetecsa.sdk.access.utils.ExceptionHandler
import io.github.suitetecsa.sdk.exception.NotLoggedInException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

internal class ErrorParserImpl : ErrorParser {
    private val regex = """alert\("(?<reason>[^"]*?)"\)""".toRegex()

    /**
     * Parses the error message from the given HTML document.
     *
     * @param document The HTML document to parse.
     * @return The parsed error message, or null if no error message is found.
     */
    private fun parseError(document: Document) = document.select("script[type='text/javascript']")
        .last()?.let { lastScript ->
            regex.find(lastScript.data().trim { it <= ' ' })?.let { result ->
                result.groups["reason"]?.value
            }
        }

    @Throws(NotLoggedInException::class)
    override fun <T : Exception> throwExceptionOnFailure(
        httpResponse: HttpResponse,
        message: String,
        exceptionHandler: ExceptionHandler<T>
    ): HttpResponse {
        parseError(Jsoup.parse(httpResponse.text))?.let {
            throw exceptionHandler.handleException(message, listOf(it))
        }
        return httpResponse
    }
}
