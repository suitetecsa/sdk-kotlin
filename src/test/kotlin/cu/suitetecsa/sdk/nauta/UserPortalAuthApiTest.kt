package cu.suitetecsa.sdk.nauta

import cu.suitetecsa.sdk.nauta.exception.NotLoggedInException
import cu.suitetecsa.sdk.nauta.model.NautaUser
import cu.suitetecsa.sdk.nauta.scraper.AuthUserPortalScraper
import cu.suitetecsa.sdk.nauta.util.action.GetCaptcha
import cu.suitetecsa.sdk.nauta.util.action.LoadUserInformation
import cu.suitetecsa.sdk.nauta.util.action.Login
import cu.suitetecsa.sdk.network.HttpResponse
import cu.suitetecsa.sdk.network.PortalCommunicator
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class UserPortalAuthApiTest {

    // Tests that the credentials can be set and the user can login successfully
    @Test
    fun test_set_credentials_and_login_successfully() {
        // Create a mock PortalCommunicator
        val communicator = mock<PortalCommunicator>()
        val sessionManager = DefaultUserPortalSessionManager
            .Builder()
            .withCommunicator(communicator)
            .build()

        // Create a mock AuthUserPortalScraper
        val scraper = mock<AuthUserPortalScraper>()

        // Create a UserPortalAuthApi instance using the Builder with the mocks
        val authApi = UserPortalAuthApi.Builder()
            .withSessionManager(sessionManager)
            .withScraper(scraper)
            .build()

        val userResult = NautaUser("", "", "", "", "", "", "", "", "offer")

        whenever(
            sessionManager.communicator.performRequest(any<String>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.success(""))
        whenever(
            sessionManager.communicator.performRequest(any<Login>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.success(userResult))

        // Mock the parseNautaUser function
        whenever(scraper.parseNautaUser(any(), any())).thenReturn(userResult)

        // Call the login function and assert the result
        val result = authApi.login("", "", "captcha")
        assertTrue(result.isSuccess)
        assertEquals(userResult, result.getOrNull())
        assertEquals(true, sessionManager.isNautaHome)
    }

    // Tests that the captcha image can be loaded successfully
    @Test
    fun test_load_captcha_image_successfully() {
        // Create a mock PortalCommunicator
        val communicator = mock<PortalCommunicator>()
        val sessionManager = DefaultUserPortalSessionManager
            .Builder()
            .withCommunicator(communicator)
            .build()

        // Create a mock AuthUserPortalScraper
        val scraper = mock<AuthUserPortalScraper>()

        // Create a UserPortalAuthApi instance using the Builder with the mocks
        val authApi = UserPortalAuthApi.Builder()
            .withSessionManager(sessionManager)
            .withScraper(scraper)
            .build()

        // Mock the GetCaptcha action
        whenever(communicator.performRequest(any<GetCaptcha>(), any<(HttpResponse) -> Any>())).thenReturn(
            Result.success(ByteArray(0))
        )

        // Call the captchaImage property and assert the result
        val result = authApi.captchaImage
        assertTrue(result.isSuccess)
    }

    // Tests that the user information can be loaded successfully
    @Test
    fun test_load_user_information_successfully() {
        // Create a mock PortalCommunicator
        val communicator = mock<PortalCommunicator>()
        val sessionManager = DefaultUserPortalSessionManager
            .Builder()
            .withCommunicator(communicator)
            .build()

        // Create a mock AuthUserPortalScraper
        val scraper = mock<AuthUserPortalScraper>()

        // Create a UserPortalAuthApi instance using the Builder with the mocks
        val authApi = UserPortalAuthApi.Builder()
            .withSessionManager(sessionManager)
            .withScraper(scraper)
            .build()

        val userResult = NautaUser("", "", "", "", "", "", "", "", "offer")

        // Set the CSRF token
        whenever(
            communicator.performRequest(any<String>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.success("csrf"))
        authApi.login("username", "password", "captcha")

        whenever(
            communicator.performRequest(
                any<LoadUserInformation>(),
                any<(HttpResponse) -> Any>()
            )
        ).thenReturn(Result.success(userResult))

        // Call the userInformation property and assert the result
        val result = authApi.userInformation
        assertTrue(result.isSuccess)
        assertEquals(userResult, result.getOrNull())
        assertTrue(sessionManager.isNautaHome!!)
    }

    // Tests that an exception is thrown when trying to load user information without logging in
    @Test
    fun test_load_user_information_without_logging_in() {
        // Create a mock PortalCommunicator
        val communicator = mock<PortalCommunicator>()
        val sessionManager = DefaultUserPortalSessionManager
            .Builder()
            .withCommunicator(communicator)
            .build()

        // Create a mock AuthUserPortalScraper
        val scraper = mock<AuthUserPortalScraper>()

        // Create a UserPortalAuthApi instance using the Builder with the mocks
        val authApi = UserPortalAuthApi.Builder()
            .withSessionManager(sessionManager)
            .withScraper(scraper)
            .build()

        // Call the userInformation property and assert the exception
        assertThrows<NotLoggedInException> { authApi.userInformation.onFailure { throw it } }
    }
}
