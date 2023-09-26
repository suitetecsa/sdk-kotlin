package cu.suitetecsa.sdk.nauta

import cu.suitetecsa.sdk.nauta.exception.LoadInfoException
import cu.suitetecsa.sdk.nauta.exception.LogoutException
import cu.suitetecsa.sdk.nauta.exception.NautaAttributeException
import cu.suitetecsa.sdk.nauta.exception.NautaGetInfoException
import cu.suitetecsa.sdk.nauta.model.AccountInfo
import cu.suitetecsa.sdk.nauta.model.DataSession
import cu.suitetecsa.sdk.nauta.model.NautaConnectInformation
import cu.suitetecsa.sdk.nauta.scraper.ConnectPortalScraper
import cu.suitetecsa.sdk.nauta.util.action.CheckConnection
import cu.suitetecsa.sdk.nauta.util.action.GetPage
import cu.suitetecsa.sdk.nauta.util.action.LoadUserInformation
import cu.suitetecsa.sdk.nauta.util.action.Login
import cu.suitetecsa.sdk.network.Action
import cu.suitetecsa.sdk.network.HttpResponse
import cu.suitetecsa.sdk.network.PortalCommunicator
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ConnectApiTest {

    // Tests that ConnectApi can be instantiated with a PortalCommunicator and a ConnectPortalScraper
    @Test
    fun test_instantiation_with_portal_communicator_and_scraper() {
        // Create mock PortalCommunicator and ConnectPortalScraper
        val communicator = mock<PortalCommunicator>()
        val scraper = mock<ConnectPortalScraper>()

        // Instantiate ConnectApi with the mock objects
        val connectApi = ConnectApi.Builder()
            .withCommunicator(communicator)
            .withScraper(scraper)
            .build()

        // Assert that the ConnectApi object is not null
        assertNotNull(connectApi)
    }

    // Tests that setCredentials sets the username and password
    @Test
    fun test_set_credentials() {
        // Create a ConnectApi object
        val connectApi = ConnectApi.Builder().build()

        // Set the credentials
        connectApi.setCredentials("username", "password")

        // Assert that the username and password are set correctly
        assertEquals("username", connectApi.username)
        assertEquals("password", connectApi.password)
    }

    // Tests that connect connects to the Nauta network and returns "Connected"
    @Test
    fun test_connect() {
        // Create mock PortalCommunicator and ConnectPortalScraper
        val communicator = mock<PortalCommunicator>()
        val scraper = mock<ConnectPortalScraper>()

        // Create a ConnectApi object with the mock objects
        val connectApi = ConnectApi.Builder()
            .withCommunicator(communicator)
            .withScraper(scraper)
            .build()

        // Set the credentials
        connectApi.setCredentials("username", "password")

        // Mock the performAction method of the communicator to return a successful Result
        val isConnected = Result.success(false)
        val result = Result.success("AttributeUUID")

        whenever(
            communicator.performAction(any<CheckConnection>(), any<(HttpResponse) -> Boolean>())
        ).thenReturn(isConnected)

        whenever(
            communicator.performAction(any<GetPage>(), any<(HttpResponse) -> Pair<String, Map<String, String>>>())
        ).thenReturn(Result.success(Pair("", mapOf("wlanuserip" to "wlanuserip", "CSRFHW" to "CSRFHW"))))

        whenever(communicator.performAction(any<Login>(), any<(HttpResponse) -> Any>())).thenReturn(result)

        whenever(scraper.parseCheckConnections(any())).thenReturn(false)
        whenever(scraper.parseActionForm(any())).thenReturn(Pair("", emptyMap()))
        whenever(
            scraper.parseLoginForm(any())
        ).thenReturn(Pair("", mapOf("wlanuserip" to "wlanuserip", "CSRFHW" to "CSRFHW")))
        whenever(scraper.parseAttributeUUID(any())).thenReturn("AttributeUUID")

        // Call the connect method
        val connectResult = connectApi.connect()

        // Assert that the connectResult is successful and returns "Connected"
        assertTrue(connectResult.isSuccess)
        assertEquals("Connected", connectResult.getOrNull())
    }

    // Tests that disconnect disconnects from the Nauta network and returns "Disconnected"
    @Test
    fun test_disconnect() {
        // Create mock PortalCommunicator and ConnectPortalScraper
        val communicator = mock<PortalCommunicator>()
        val scraper = mock<ConnectPortalScraper>()

        // Create a ConnectApi object with the mock objects
        val connectApi = ConnectApi.Builder()
            .withCommunicator(communicator)
            .withScraper(scraper)
            .build()

        // Set the credentials
        connectApi.setCredentials("username", "password")

        // Set the dataSession
        connectApi.dataSession = DataSession("username", "csrfHw", "wlanUserIp", "attributeUUID")

        // Mock the performAction method of the communicator to return a successful Result
        val result = Result.success(true)
        whenever(communicator.performAction(any<Action>(), any<(HttpResponse) -> Any>())).thenReturn(result)

        // Call the disconnect method
        val disconnectResult = connectApi.disconnect()

        // Assert that the disconnectResult is successful and returns "Disconnected"
        assertTrue(disconnectResult.isSuccess)
        assertEquals("Disconnected", disconnectResult.getOrNull())
    }

    // Tests that isConnected returns true if connected to the Nauta network
    @Test
    fun test_is_connected() {
        // Create mock PortalCommunicator and ConnectPortalScraper
        val communicator = mock<PortalCommunicator>()
        val scraper = mock<ConnectPortalScraper>()

        // Create a ConnectApi object with the mock objects
        val connectApi = ConnectApi.Builder()
            .withCommunicator(communicator)
            .withScraper(scraper)
            .build()

        // Mock the performAction method of the communicator to return a successful Result
        val result = Result.success(true)
        whenever(communicator.performAction(any<Action>(), any<(HttpResponse) -> Any>())).thenReturn(result)

        // Call the isConnected property
        val isConnectedResult = connectApi.isConnected

        // Assert that the isConnectedResult is successful and returns true
        assertTrue(isConnectedResult.isSuccess)
        assertTrue(isConnectedResult.getOrNull()!!)
    }

    // Tests that remainingTime returns the remaining time in seconds if connected to the Nauta network
    @Test
    fun test_remaining_time() {
        // Create mock PortalCommunicator and ConnectPortalScraper
        val communicator = mock<PortalCommunicator>()
        val scraper = mock<ConnectPortalScraper>()

        // Create a ConnectApi object with the mock objects
        val connectApi = ConnectApi.Builder()
            .withCommunicator(communicator)
            .withScraper(scraper)
            .build()

        // Set the credentials
        connectApi.setCredentials("username", "password")

        // Set the dataSession
        connectApi.dataSession = DataSession("username", "csrfHw", "wlanUserIp", "attributeUUID")

        // Mock the performAction method of the communicator to return a successful Result
        val isConnected = Result.success(true)
        val result = Result.success(3600L)
        whenever(
            communicator.performAction(any<CheckConnection>(), any<(HttpResponse) -> Any>())
        ).thenReturn(isConnected)
        whenever(
            communicator.performAction(any<LoadUserInformation>(), any<(HttpResponse) -> Any>())
        ).thenReturn(result)

        // Call the remainingTime property
        val remainingTimeResult = connectApi.remainingTime

        // Assert that the remainingTimeResult is successful and returns the remaining time in seconds
        assertTrue(remainingTimeResult.isSuccess)
        assertEquals(3600L, remainingTimeResult.getOrNull())
    }

    // Test that the connectInformation function returns the NautaConnectInformation if connected to the Nauta network
    @Test
    fun test_connectInformation_returns_NautaConnectInformation_if_connected() {
        // Create a mock PortalCommunicator
        val communicator = mock<PortalCommunicator>()

        // Create a mock ConnectPortalScraper
        val scraper = mock<ConnectPortalScraper>()

        // Create a ConnectApi instance with the mock objects
        val connectApi = ConnectApi.Builder().withCommunicator(communicator).withScraper(scraper).build()

        // Set up the mock objects to return the expected values
        val expectedConnectInformation = NautaConnectInformation(
            accountInfo = AccountInfo("", "", "", ""),
            lastConnections = emptyList()
        )

        whenever(
            communicator.performAction(
                any<CheckConnection>(),
                any<(HttpResponse) -> Pair<String, Map<String, String>>>()
            )
        ).thenReturn(Result.success(Pair("", emptyMap())))

        whenever(
            communicator.performAction(any<GetPage>(), any<(HttpResponse) -> Pair<String, Map<String, String>>>())
        ).thenReturn(Result.success(Pair("", mapOf("wlanuserip" to "wlanuserip", "CSRFHW" to "CSRFHW"))))

        whenever(
            communicator.performAction(any<LoadUserInformation>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.success(expectedConnectInformation))

        connectApi.setCredentials("username", "password")

        // Call the connectInformation function
        val result = connectApi.connectInformation

        // Assert that the result is the expected NautaConnectInformation
        assertEquals(Result.success(expectedConnectInformation), result)
    }

    // Test that the connect function throws a NautaAttributeException if the username or password is null or blank.
    @Test
    fun test_connect_throws_NautaAttributeException_if_username_or_password_is_null_or_blank() {
        // Create mock PortalCommunicator and ConnectPortalScraper
        val communicator = mock<PortalCommunicator>()
        val scraper = mock<ConnectPortalScraper>()

        // Create a ConnectApi object with the mock objects
        val connectApi = ConnectApi.Builder()
            .withCommunicator(communicator)
            .withScraper(scraper)
            .build()

        // Arrange
        val username = ""
        val password = ""
        connectApi.setCredentials(username, password)

        whenever(
            communicator.performAction(any<CheckConnection>(), any<(HttpResponse) -> Boolean>())
        ).thenReturn(Result.success(false))

        // Act
        val result = connectApi.connect()

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NautaAttributeException)
    }

    // Test that the connect function throws a LoadInfoException if the attributeUUID cannot be parsed
    @Test
    fun test_connect_throws_LoadInfoException_if_attributeUUID_cannot_be_parsed() {
        // Create a mock PortalCommunicator
        val communicator = mockk<PortalCommunicator>()

        // Create a mock ConnectPortalScraper
        val scraper = mockk<ConnectPortalScraper>()

        // Create a ConnectApi instance with the mocks
        val connectApi = ConnectApi.Builder().withCommunicator(communicator).withScraper(scraper).build()

        // Set up the mocks to return the necessary values
        every {
            communicator.performAction(any<Action>(), any<(HttpResponse) -> Any>())
        } throws LoadInfoException("Failed to parse attributeUUID")

        // Set the credentials
        connectApi.setCredentials("username", "password")

        // Assert that connect throws a LoadInfoException
        assertThrows<LoadInfoException> { connectApi.connect() }
    }

    // Test that the disconnect function throws a LogoutException if the user is not connected.
    @Test
    fun disconnect_throws_logout_exception_if_not_connected() {
        // Arrange
        val communicator = mock<PortalCommunicator>()
        val scraper = mock<ConnectPortalScraper>()
        val connectApi = ConnectApi.Builder()
            .withCommunicator(communicator)
            .withScraper(scraper)
            .build()

        val username = "test_username"
        val password = "test_password"
        connectApi.setCredentials(username, password)

        whenever(communicator.performAction(any<CheckConnection>(), any<(HttpResponse) -> Any>()))
            .thenReturn(Result.success(false))

        // Act and Assert
        assertThrows<LogoutException> {
            connectApi.disconnect().onFailure { throw it }
        }
    }

    // Test that the disconnect function throws a NautaAttributeException if the dataSession is null
    @Test
    fun test_disconnect_throws_exception_if_dataSession_is_null() {
        // Arrange
        val communicator = mock<PortalCommunicator>()
        val scraper = mock<ConnectPortalScraper>()
        val connectApi = ConnectApi.Builder()
            .withCommunicator(communicator)
            .withScraper(scraper)
            .build()

        // Mock the performAction function of the communicator to return a Result.success with a true value
        whenever(
            communicator.performAction(any<Action>(), any<(HttpResponse) -> Any>())
        ).thenReturn(
            Result.success(true)
        )

        // Act and Assert
        assertThrows<NautaAttributeException> {
            connectApi.disconnect().onFailure { throw it }
        }
    }

    // Test that the remainingTime function throws a NautaGetInfoException if the user is not connected
    @Test
    fun remaining_time_throws_exception_if_not_connected() {
        // Create a mock PortalCommunicator
        val communicator = mock<PortalCommunicator>()

        // Create a mock ConnectPortalScraper
        val scraper = mock<ConnectPortalScraper>()

        // Create an instance of ConnectApi with the mock objects
        val connectApi = ConnectApi.Builder().withCommunicator(communicator).withScraper(scraper).build()

        // Set the username and password
        connectApi.setCredentials("username", "password")

        // Mock the performAction function of the communicator to return a Result.failure with a NautaGetInfoException
        whenever(
            communicator.performAction(any<Action>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.success(false))

        // Call the remainingTime function and assert that it throws a NautaGetInfoException
        assertThrows<NautaGetInfoException> { connectApi.remainingTime.onFailure { throw it } }
    }

    // Test that the remainingTime function throws a NautaAttributeException if the dataSession is null
    @Test
    fun test_remainingTimeThrowsExceptionIfDataSessionIsNull() {
        // Arrange
        val communicator = mock<PortalCommunicator>()
        val scraper = mock<ConnectPortalScraper>()
        val connectApi = ConnectApi.Builder()
            .withCommunicator(communicator)
            .withScraper(scraper)
            .build()

        whenever(communicator.performAction(any<CheckConnection>(), any<(HttpResponse) -> Any>()))
            .thenReturn(Result.success(true))

        // Act and Assert
        assertThrows<NautaAttributeException> {
            connectApi.remainingTime.onFailure { throw it }
        }
    }

    // Test that the connectInformation function throws a NautaAttributeException if the username or password is null
    // or blank.
    @Test
    fun test_connectInformation_throws_NautaAttributeException_if_username_or_password_is_null_or_blank() {
        // Arrange
        val username = ""
        val password = ""
        val communicator = mock<PortalCommunicator>()
        val scraper = mock<ConnectPortalScraper>()
        val connectApi = ConnectApi.Builder()
            .withCommunicator(communicator)
            .withScraper(scraper)
            .build()

        // Act and Assert
        assertThrows<NautaAttributeException> {
            connectApi.setCredentials(username, password)
            connectApi.connectInformation.onFailure { throw it }
        }
    }

    // Test that the connectInformation function throws a NautaAttributeException if csrfHw is null or blank
    @Test
    fun test_connectInformation_throws_NautaAttributeException_if_csrfHw_is_null_or_blank() {
        // Arrange
        val communicator = mockk<PortalCommunicator>()
        val scraper = mockk<ConnectPortalScraper>()
        val connectApi = ConnectApi.Builder()
            .withCommunicator(communicator)
            .withScraper(scraper)
            .build()

        // Act and Assert
        assertThrows<NautaAttributeException> {
            connectApi.connectInformation.onFailure { throw it }
        }
    }
}
