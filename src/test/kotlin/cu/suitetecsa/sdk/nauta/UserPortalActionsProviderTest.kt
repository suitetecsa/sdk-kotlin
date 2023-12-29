package cu.suitetecsa.sdk.nauta

import cu.suitetecsa.sdk.nauta.model.Connection
import cu.suitetecsa.sdk.nauta.model.ConnectionsSummary
import cu.suitetecsa.sdk.nauta.model.RechargesSummary
import cu.suitetecsa.sdk.nauta.model.TransfersSummary
import cu.suitetecsa.sdk.nauta.scraper.ActionsParser
import cu.suitetecsa.sdk.nauta.scraper.ActionsSummaryParser
import cu.suitetecsa.sdk.nauta.scraper.ErrorParser
import cu.suitetecsa.sdk.nauta.scraper.TokenParser
import cu.suitetecsa.sdk.nauta.util.action.GetSummary
import cu.suitetecsa.sdk.network.HttpResponse
import cu.suitetecsa.sdk.network.PortalCommunicator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

typealias AnyTransform = (HttpResponse) -> Any

class UserPortalActionsProviderTest {

    // Successfully get connections summary
    @Test
    fun test_get_connections_summary_success() {
        // Mock the dependencies
        val communicator = mock<PortalCommunicator>()
        val tokenParser = mock<TokenParser>()
        val sessionManager = DefaultUserPortalSessionManager
            .Builder()
            .withCommunicator(communicator)
            .withTokenParser(tokenParser)
            .build()
        val errorParser = mock<ErrorParser>()
        val actionsParser = mock<ActionsParser>()
        val summaryParser = mock<ActionsSummaryParser>()

        // Create an instance of UserPortalActionsProvider with the mocked dependencies
        val provider = UserPortalActionsProvider.Builder()
            .withSessionManager(sessionManager)
            .withErrorParser(errorParser)
            .withActionsParser(actionsParser)
            .withSummaryParser(summaryParser)
            .build()

        // Mock the necessary method calls and responses
        val year = 2022
        val month = 1
        val actionUrl = "/useraaa/service_detail/"
        val csrfToken = "csrf_token"
        val connectionsSummary = ConnectionsSummary(5, "", 0L, 0f, 0.0, 0.0, 0.0)

        whenever(communicator.performRequest(eq("https://www.portal.nauta.cu$actionUrl"), any<(AnyTransform)>()))
            .thenReturn(Result.success(csrfToken))
        whenever(communicator.performRequest(any<GetSummary>(), any<(AnyTransform)>())).thenReturn(
            Result.success(connectionsSummary)
        )
        whenever(tokenParser.parseCsrfToken(any())).thenReturn(csrfToken)

        // Call the method under test
        val result = provider.getConnectionsSummary(year, month)

        // Verify the expected method calls and responses
        verify(communicator).performRequest(eq("https://www.portal.nauta.cu$actionUrl"), any<(AnyTransform)>())

        // Assert the result
        assertTrue(result.isSuccess)
        assertEquals(connectionsSummary, result.getOrNull())
    }

    // Successfully get connections
    @Test
    fun test_get_connections_success() {
        // Mock the dependencies
        val communicator = mock<PortalCommunicator>()
        val tokenParser = mock<TokenParser>()
        val sessionManager = DefaultUserPortalSessionManager
            .Builder()
            .withCommunicator(communicator)
            .withTokenParser(tokenParser)
            .build()
        val errorParser = mock<ErrorParser>()
        val actionsParser = mock<ActionsParser>()
        val summaryParser = mock<ActionsSummaryParser>()

        // Create an instance of UserPortalActionsProvider with the mocked dependencies
        val provider = UserPortalActionsProvider.Builder()
            .withSessionManager(sessionManager)
            .withErrorParser(errorParser)
            .withActionsParser(actionsParser)
            .withSummaryParser(summaryParser)
            .build()

        // Mock the necessary method calls and responses
        val summary = ConnectionsSummary(5, "2023-09", 0L, 0f, 0.0, 0.0, 0.0)
        val csrfToken = "csrf_token"
        val connections = listOf<Connection>()

        whenever(communicator.performRequest(any<String>(), any<AnyTransform>())).thenReturn(Result.success(csrfToken))

        // Call the method under test
        val result = provider.getConnections(summary)

        // Verify the expected method calls and responses
        verify(communicator)
            .performRequest(
                eq("https://www.portal.nauta.cu/useraaa/service_detail_list/2023-09/5"),
                any<AnyTransform>()
            )

        // Assert the result
        assertTrue(result.isSuccess)
        assertEquals(connections, result.getOrNull())
    }

    // Successfully get recharges summary
    @Test
    fun test_get_recharges_summary_success() {
        // Mock the dependencies
        val communicator = mock<PortalCommunicator>()
        val tokenParser = mock<TokenParser>()
        val sessionManager = DefaultUserPortalSessionManager
            .Builder()
            .withCommunicator(communicator)
            .withTokenParser(tokenParser)
            .build()
        val errorParser = mock<ErrorParser>()
        val actionsParser = mock<ActionsParser>()
        val summaryParser = mock<ActionsSummaryParser>()

        // Create an instance of UserPortalActionsProvider with the mocked dependencies
        val provider = UserPortalActionsProvider.Builder()
            .withSessionManager(sessionManager)
            .withErrorParser(errorParser)
            .withActionsParser(actionsParser)
            .withSummaryParser(summaryParser)
            .build()

        // Mock the necessary method calls and responses
        val year = 2022
        val month = 1
        val actionUrl = "/useraaa/recharge_detail/"
        val csrfToken = "csrf_token"
        val rechargesSummary = RechargesSummary(5, "", 0f)

        whenever(communicator.performRequest(any<String>(), any<AnyTransform>())).thenReturn(
            Result.success(csrfToken)
        )
        whenever(communicator.performRequest(any<GetSummary>(), any<AnyTransform>())).thenReturn(
            Result.success(rechargesSummary)
        )

        // Call the method under test
        val result = provider.getRechargesSummary(year, month)

        // Verify the expected method calls and responses
        verify(communicator).performRequest(eq("https://www.portal.nauta.cu$actionUrl"), any<AnyTransform>())

        // Assert the result
        assertTrue(result.isSuccess)
        assertEquals(rechargesSummary, result.getOrNull())
    }

    // Successfully get recharges
    @Test
    fun test_get_recharges_success() {
        // Mock the dependencies
        val communicator = mock<PortalCommunicator>()
        val tokenParser = mock<TokenParser>()
        val sessionManager = DefaultUserPortalSessionManager
            .Builder()
            .withCommunicator(communicator)
            .withTokenParser(tokenParser)
            .build()
        val errorParser = mock<ErrorParser>()
        val actionsParser = mock<ActionsParser>()
        val summaryParser = mock<ActionsSummaryParser>()

        // Create an instance of UserPortalActionsProvider with the mocked dependencies
        val provider = UserPortalActionsProvider.Builder()
            .withSessionManager(sessionManager)
            .withErrorParser(errorParser)
            .withActionsParser(actionsParser)
            .withSummaryParser(summaryParser)
            .build()

        // Mock the necessary method calls and responses
        val year = 2022
        val month = 1
        val actionUrl = "/useraaa/recharge_detail/"
        val csrfToken = "csrf_token"
        val rechargesSummary = RechargesSummary(5, "", 0f)

        whenever(communicator.performRequest(any<String>(), any<AnyTransform>())).thenReturn(
            Result.success(csrfToken)
        )
        whenever(communicator.performRequest(any<GetSummary>(), any<AnyTransform>())).thenReturn(
            Result.success(rechargesSummary)
        )

        // Call the method under test
        val result = provider.getRechargesSummary(year, month)

        // Verify the expected method calls and responses
        verify(communicator).performRequest(eq("https://www.portal.nauta.cu$actionUrl"), any<AnyTransform>())

        // Assert the result
        assertTrue(result.isSuccess)
        assertEquals(rechargesSummary, result.getOrNull())
    }

    // Successfully get transfers summary
    @Test
    fun test_get_transfers_summary_success() {
        // Mock the dependencies
        val communicator = mock<PortalCommunicator>()
        val tokenParser = mock<TokenParser>()
        val sessionManager = DefaultUserPortalSessionManager
            .Builder()
            .withCommunicator(communicator)
            .withTokenParser(tokenParser)
            .build()
        val errorParser = mock<ErrorParser>()
        val actionsParser = mock<ActionsParser>()
        val summaryParser = mock<ActionsSummaryParser>()

        // Create an instance of UserPortalActionsProvider with the mocked dependencies
        val provider = UserPortalActionsProvider.Builder()
            .withSessionManager(sessionManager)
            .withErrorParser(errorParser)
            .withActionsParser(actionsParser)
            .withSummaryParser(summaryParser)
            .build()

        // Mock the necessary method calls and responses
        val year = 2022
        val month = 1
        val actionUrl = "/useraaa/transfer_detail/"
        val csrfToken = "csrf_token"
        val transfersSummary = TransfersSummary(5, "", 0f)

        whenever(communicator.performRequest(any<String>(), any<AnyTransform>()))
            .thenReturn(Result.success(csrfToken))
        whenever(communicator.performRequest(any<GetSummary>(), any<AnyTransform>()))
            .thenReturn(Result.success(transfersSummary))

        // Call the method under test
        val result = provider.getTransfersSummary(year, month)

        // Verify the expected method calls and responses
        verify(communicator).performRequest(eq("https://www.portal.nauta.cu$actionUrl"), any<AnyTransform>())

        // Assert the result
        assertTrue(result.isSuccess)
        assertEquals(transfersSummary, result.getOrNull())
    }
}
