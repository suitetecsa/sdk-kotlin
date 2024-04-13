package io.github.suitetecsa.sdk.network

import io.github.suitetecsa.sdk.access.utils.ExceptionHandler
import io.github.suitetecsa.sdk.exception.NautaException
import org.jsoup.Connection

private const val STATUS_REDIRECT_RANGE_END = 399
private const val STATUS_REDIRECT_RANGE_START = 300
private const val STATUS_OK_RANGE_END = 299
private const val STATUS_OK_RANGE_START = 200

object ResponseUtils {
    /**
     * Esta función es un método estático llamado throwExceptionOnFailure que es una extensión de la clase Response de
     * Connection.
     * Lanza una excepción si la respuesta indica un fallo basado en el código de estado.
     *
     * @param message           El mensaje de error a incluir en la excepción.
     */
    @JvmStatic
    @Throws(NautaException::class)
    fun throwExceptionOnFailure(response: Connection.Response, message: String) {
        throwExceptionOnFailure(response, message, ExceptionHandler { NautaException(it) })
    }

    /**
     * Esta función es un método estático llamado throwExceptionOnFailure que es una extensión de la clase Response de
     * Connection.
     * Lanza una excepción si la respuesta indica un fallo basado en el código de estado.
     *
     * @param message           El mensaje de error a incluir en la excepción.
     * @param exceptionHandler  La factoría para crear la excepción (opcional).
     */
    private fun <T : Exception> throwExceptionOnFailure(
        response: Connection.Response,
        message: String?,
        exceptionHandler: ExceptionHandler<T>
    ) {
        val statusCode = response.statusCode()
        if (statusCode !in STATUS_OK_RANGE_START..STATUS_OK_RANGE_END &&
            !(statusCode in STATUS_REDIRECT_RANGE_START..STATUS_REDIRECT_RANGE_END && response.hasHeader("Location"))
        ) {
            throw exceptionHandler
                .handleException(message!!, listOf(response.statusMessage()))
        }
    }
}

/**
 * Esta función es un método estático llamado throwExceptionOnFailure que es una extensión de la clase Response de
 * Connection.
 * Lanza una excepción si la respuesta indica un fallo basado en el código de estado.
 *
 * @param message           El mensaje de error a incluir en la excepción.
 */
@Throws(NautaException::class)
fun Connection.Response.throwExceptionOnFailure(message: String) {
    ResponseUtils.throwExceptionOnFailure(this, message)
}
