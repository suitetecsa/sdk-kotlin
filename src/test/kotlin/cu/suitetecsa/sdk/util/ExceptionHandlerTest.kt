package cu.suitetecsa.sdk.util

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExceptionHandlerTest {

    private lateinit var exceptionHandler: ExceptionHandler
    private lateinit var exceptionFactory: ExceptionFactory

    @BeforeEach
    fun setUp() {
        exceptionFactory = ExceptionFactoryImpl(Exception::class.java)
        exceptionHandler = ExceptionHandler.Builder().setExceptionFactory(exceptionFactory).build()
    }

    @AfterEach
    fun tearDown() {
        // Clean up resources if needed
    }

    @Test
    fun testHandleExceptionWhenErrorsNotEmptyThenReturnsCorrectMessage() {
        // Arrange
        val message = "Test message"
        val errors = listOf("Error 1", "Error 2", "Error 3")

        // Act
        val exception = exceptionHandler.handleException(message, errors)

        // Assert
        val expectedMessage = "$message :: ${errors.joinToString("; ")}"
        assertEquals(expectedMessage, exception.message)
    }

    @Test
    fun testHandleExceptionWhenErrorsEmptyThenReturnsCorrectMessage() {
        // Arrange
        val message = "Test message"
        val errors = emptyList<String>()

        // Act
        val exception = exceptionHandler.handleException(message, errors)

        // Assert
        val expectedMessage = "$message :: No specific error message"
        assertEquals(expectedMessage, exception.message)
    }
}
