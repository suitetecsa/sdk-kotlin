package cu.suitetecsa.sdk.network

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsoupConnectionFactoryTest {

    private lateinit var connection: Connection
    private lateinit var service: ConnectionFactory
    private val url = "http://test.com"
    private val requestData = mapOf("key" to "value")
    private val cookies = mapOf("cookie" to "value")

    @BeforeEach
    fun setup() {
        connection = mockk(relaxed = true)
        service = JsoupConnectionFactory

        mockkStatic(Jsoup::class)
        every { Jsoup.connect(url) } returns connection
        every { connection.userAgent(any()) } returns connection
        every { connection.headers(any()) } returns connection
        every { connection.data(requestData) } returns connection
        every { connection.cookies(cookies) } returns connection
    }

    @Test
    fun testCreateConnectionWhenAllParametersNonNullThenCorrectMethodsCalled() {
        // Arrange
        mockkStatic(Jsoup::class)
        every { Jsoup.connect(url) } returns connection
        every { connection.userAgent(any()) } returns connection
        every { connection.headers(any()) } returns connection
        every { connection.data(requestData) } returns connection
        every { connection.cookies(cookies) } returns connection

        // Act
        JsoupConnectionFactory.createConnection(url, requestData, cookies)

        // Assert
        verify { connection.data(requestData) }
        verify { connection.cookies(cookies) }
    }

    @Test
    fun testCreateConnectionWhenRequestDataNullThenDataMethodNotCalled() {
        // Arrange
        mockkStatic(Jsoup::class)
        every { Jsoup.connect(url) } returns connection
        every { connection.userAgent(any()) } returns connection
        every { connection.headers(any()) } returns connection
        every { connection.data(requestData) } returns connection
        every { connection.cookies(cookies) } returns connection

        // Act
        JsoupConnectionFactory.createConnection(url, null, cookies)

        // Assert
        verify(exactly = 0) { connection.data(requestData) }
    }

    @Test
    fun testCreateConnectionWhenCookiesNullThenCookiesMethodNotCalled() {
        // Arrange
        mockkStatic(Jsoup::class)
        every { Jsoup.connect(url) } returns connection
        every { connection.userAgent(any()) } returns connection
        every { connection.headers(any()) } returns connection
        every { connection.data(requestData) } returns connection
        every { connection.cookies(cookies) } returns connection

        // Act
        service.createConnection(url, requestData, null)

        // Assert
        verify(exactly = 0) { connection.cookies(cookies) }
    }

    @Test
    fun testCreateConnectionWhenCookiesEmptyThenCookiesMethodNotCalled() {
        // Arrange
        mockkStatic(Jsoup::class)
        every { Jsoup.connect(url) } returns connection
        every { connection.userAgent(any()) } returns connection
        every { connection.headers(any()) } returns connection
        every { connection.data(requestData) } returns connection
        every { connection.cookies(cookies) } returns connection

        // Act
        service.createConnection(url, requestData, emptyMap())

        // Assert
        verify(exactly = 0) { connection.cookies(cookies) }
    }
}
