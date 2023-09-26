package cu.suitetecsa.sdk.network

import org.jsoup.Connection
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DefaultSessionTest {

    // Tests that a GET request is successfully executed with all parameters provided
    @Test
    fun test_successful_get_request_with_all_parameters_provided() {
        // Mock the necessary dependencies
        val connectionFactory = mock<ConnectionFactory>()
        val connection = mock<Connection>()
        val response = mock<Connection.Response>()

        // Set up the mocks
        whenever(connectionFactory.createConnection(any(), any(), any())).thenReturn(connection)
        whenever(connection.ignoreContentType(anyBoolean())).thenReturn(connection)
        whenever(connection.timeout(anyInt())).thenReturn(connection)
        whenever(connection.method(Connection.Method.GET)).thenReturn(connection)
        whenever(connection.execute()).thenReturn(response)

        // Set up the response
        whenever(response.statusCode()).thenReturn(200)
        whenever(response.statusMessage()).thenReturn("OK")
        whenever(response.bodyAsBytes()).thenReturn(byteArrayOf())
        whenever(response.cookies()).thenReturn(emptyMap())

        // Create the session
        val session = DefaultSession.Builder().connectionFactory(connectionFactory).build()

        // Call the method under test
        val result = session.get("https://example.com", mapOf("param1" to "value1"), true, 5000)

        // Verify the result
        assertTrue(result.isSuccess)
        assertEquals(200, result.getOrNull()?.statusCode)
        assertEquals("OK", result.getOrNull()?.statusMessage)
        assertArrayEquals(byteArrayOf(), result.getOrNull()?.content)
        assertEquals(emptyMap<String, String>(), result.getOrNull()?.cookies)
    }

    // Tests that a GET request is successfully executed with only the required parameters provided
    @Test
    fun test_successful_get_request_with_only_required_parameters_provided() {
        // Mock the necessary dependencies
        val connectionFactory = mock<ConnectionFactory>()
        val connection = mock<Connection>()
        val response = mock<Connection.Response>()

        // Set up the mocks
        whenever(connectionFactory.createConnection(any(), anyOrNull(), any())).thenReturn(connection)
        whenever(connection.ignoreContentType(false)).thenReturn(connection)
        whenever(connection.timeout(anyInt())).thenReturn(connection)
        whenever(connection.method(Connection.Method.GET)).thenReturn(connection)
        whenever(connection.execute()).thenReturn(response)

        // Set up the response
        whenever(response.statusCode()).thenReturn(200)
        whenever(response.statusMessage()).thenReturn("OK")
        whenever(response.bodyAsBytes()).thenReturn(byteArrayOf())
        whenever(response.cookies()).thenReturn(emptyMap())

        // Create the session
        val session = DefaultSession.Builder().connectionFactory(connectionFactory).build()

        // Call the method under test
        val result = session.get("https://example.com", null, false)

        // Verify the result
        assertTrue(result.isSuccess)
        assertEquals(200, result.getOrNull()?.statusCode)
        assertEquals("OK", result.getOrNull()?.statusMessage)
        assertArrayEquals(byteArrayOf(), result.getOrNull()?.content)
        assertEquals(emptyMap<String, String>(), result.getOrNull()?.cookies)
    }

    // Tests that a POST request is successfully executed with all parameters provided
    @Test
    fun test_successful_post_request_with_all_parameters_provided() {
        // Mock the necessary dependencies
        val connectionFactory = mock<ConnectionFactory>()
        val connection = mock<Connection>()
        val response = mock<Connection.Response>()

        // Set up the mocks
        whenever(connectionFactory.createConnection(any(), any(), any())).thenReturn(connection)
        whenever(connection.method(Connection.Method.POST)).thenReturn(connection)
        whenever(connection.execute()).thenReturn(response)

        // Set up the response
        whenever(response.statusCode()).thenReturn(200)
        whenever(response.statusMessage()).thenReturn("OK")
        whenever(response.bodyAsBytes()).thenReturn(byteArrayOf())
        whenever(response.cookies()).thenReturn(emptyMap())

        // Create the session
        val session = DefaultSession.Builder().connectionFactory(connectionFactory).build()

        // Call the method under test
        val result = session.post("https://example.com", mapOf("param1" to "value1"))

        // Verify the result
        assertTrue(result.isSuccess)
        assertEquals(200, result.getOrNull()?.statusCode)
        assertEquals("OK", result.getOrNull()?.statusMessage)
        assertArrayEquals(byteArrayOf(), result.getOrNull()?.content)
        assertEquals(emptyMap<String, String>(), result.getOrNull()?.cookies)
    }

    // Tests that a POST request is successfully executed with only the required parameters provided
    @Test
    fun test_successful_post_request_with_only_required_parameters_provided() {
        // Mock the necessary dependencies
        val connectionFactory = mock<ConnectionFactory>()
        val connection = mock<Connection>()
        val response = mock<Connection.Response>()

        // Set up the mocks
        whenever(connectionFactory.createConnection(any(), anyOrNull(), any())).thenReturn(connection)
        whenever(connection.method(Connection.Method.POST)).thenReturn(connection)
        whenever(connection.execute()).thenReturn(response)

        // Set up the response
        whenever(response.statusCode()).thenReturn(200)
        whenever(response.statusMessage()).thenReturn("OK")
        whenever(response.bodyAsBytes()).thenReturn(byteArrayOf())
        whenever(response.cookies()).thenReturn(emptyMap())

        // Create the session
        val session = DefaultSession.Builder().connectionFactory(connectionFactory).build()

        // Call the method under test
        val result = session.post("https://example.com", null)

        // Verify the result
        assertTrue(result.isSuccess)
        assertEquals(200, result.getOrNull()?.statusCode)
        assertEquals("OK", result.getOrNull()?.statusMessage)
        assertArrayEquals(byteArrayOf(), result.getOrNull()?.content)
        assertEquals(emptyMap<String, String>(), result.getOrNull()?.cookies)
    }

    // Tests that a GET request is successfully executed with empty params
    @Test
    fun test_get_request_with_empty_params() {
        // Mock the necessary dependencies
        val connectionFactory = mock<ConnectionFactory>()
        val connection = mock<Connection>()
        val response = mock<Connection.Response>()

        // Set up the mocks
        whenever(connectionFactory.createConnection(any(), any(), any())).thenReturn(connection)
        whenever(connection.ignoreContentType(false)).thenReturn(connection)
        whenever(connection.timeout(30000)).thenReturn(connection)
        whenever(connection.method(Connection.Method.GET)).thenReturn(connection)
        whenever(connection.execute()).thenReturn(response)

        // Set up the response
        whenever(response.statusCode()).thenReturn(200)
        whenever(response.statusMessage()).thenReturn("OK")
        whenever(response.bodyAsBytes()).thenReturn(byteArrayOf())
        whenever(response.cookies()).thenReturn(emptyMap())

        // Create the session
        val session = DefaultSession.Builder().connectionFactory(connectionFactory).build()

        // Call the method under test
        val result = session.get("https://example.com", emptyMap(), false)

        // Verify the result
        assertTrue(result.isSuccess)
        assertEquals(200, result.getOrNull()?.statusCode)
        assertEquals("OK", result.getOrNull()?.statusMessage)
        assertArrayEquals(byteArrayOf(), result.getOrNull()?.content)
        assertEquals(emptyMap<String, String>(), result.getOrNull()?.cookies)
    }
}
