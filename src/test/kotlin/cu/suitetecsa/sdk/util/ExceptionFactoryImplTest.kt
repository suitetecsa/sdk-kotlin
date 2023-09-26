package cu.suitetecsa.sdk.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ExceptionFactoryImplTest {

    private lateinit var exceptionFactory: ExceptionFactoryImpl

    @BeforeEach
    fun setUp() {
        exceptionFactory = ExceptionFactoryImpl(RuntimeException::class.java)
    }

    @Test
    fun testCreateExceptionWhenGivenMessageThenReturnCorrectException() {
        // Arrange
        val message = "Test message"

        // Act
        val exception = exceptionFactory.createException(message)

        // Assert
        assertTrue(exception is RuntimeException)
        assertEquals(message, exception.message)
    }
}
