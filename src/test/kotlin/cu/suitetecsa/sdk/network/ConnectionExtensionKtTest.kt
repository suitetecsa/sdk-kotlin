package cu.suitetecsa.sdk.network

import cu.suitetecsa.sdk.util.ExceptionFactory
import io.mockk.every
import io.mockk.mockk
import org.jsoup.Connection
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ConnectionExtensionKtTest {

    // Tests that the function does not throw an exception when the response status code is within the range of
    // STATUS_OK_RANGE.
    @Test
    fun test_happy_path_status_code_within_ok_range() {
        val response = mockk<Connection.Response>()
        every { response.statusCode() } returns 200
        every { response.hasHeader("Location") } returns false
        every { response.statusMessage() } returns "OK"

        assertDoesNotThrow { response.throwExceptionOnFailure("Test message") }
    }

    // Tests that the function does not throw an exception when the response status code is within the range of
    // STATUS_REDIRECT_RANGE and has a "Location" header.
    @Test
    fun test_happy_path_status_code_within_redirect_range_with_location_header() {
        val response = mockk<Connection.Response>()
        every { response.statusCode() } returns 301
        every { response.hasHeader("Location") } returns true
        every { response.statusMessage() } returns "Moved Permanently"

        assertDoesNotThrow { response.throwExceptionOnFailure("Test message") }
    }

    // Tests that the function throws an exception when the response status code is outside the range of STATUS_OK_RANGE
    // and STATUS_REDIRECT_RANGE.
    @Test
    fun test_edge_case_status_code_outside_ok_and_redirect_range() {
        val response = mockk<Connection.Response>()
        every { response.statusCode() } returns 400
        every { response.hasHeader("Location") } returns false
        every { response.statusMessage() } returns "Bad Request"

        assertThrows<Exception> { response.throwExceptionOnFailure("Test message") }
    }

    // Tests that the function throws an exception when the response status code is within the range of
    // STATUS_REDIRECT_RANGE but does not have a "Location" header.
    @Test
    fun test_edge_case_status_code_within_redirect_range_without_location_header() {
        val response = mockk<Connection.Response>()
        every { response.statusCode() } returns 302
        every { response.hasHeader("Location") } returns false
        every { response.statusMessage() } returns "Found"

        assertThrows<Exception> { response.throwExceptionOnFailure("Test message") }
    }

    // Tests that the function uses the provided exception factory when it is not null.
    @Test
    fun test_exception_factory_not_null() {
        val response = mockk<Connection.Response>()
        every { response.statusCode() } returns 500
        every { response.hasHeader("Location") } returns false
        every { response.statusMessage() } returns "Internal Server Error"

        val exceptionFactory = mockk<ExceptionFactory>()
        every { exceptionFactory.createException(any()) } returns Exception("Custom Exception")

        assertThrows<Exception> { response.throwExceptionOnFailure("Test message", exceptionFactory) }
    }

    // Tests that the function uses the default exception factory when the provided exception factory is null.
    @Test
    fun test_exception_factory_null() {
        val response = mockk<Connection.Response>()
        every { response.statusCode() } returns 500
        every { response.hasHeader("Location") } returns false
        every { response.statusMessage() } returns "Internal Server Error"

        assertThrows<Exception> { response.throwExceptionOnFailure("Test message") }
    }
}
