package cu.suitetecsa.sdk.nauta

import cu.suitetecsa.sdk.nauta.exception.TopUpBalanceException
import cu.suitetecsa.sdk.nauta.exception.TransferFundsException
import cu.suitetecsa.sdk.nauta.scraper.ErrorParser
import cu.suitetecsa.sdk.nauta.scraper.TokenParser
import cu.suitetecsa.sdk.nauta.util.action.TopUpBalance
import cu.suitetecsa.sdk.nauta.util.action.TransferFunds
import cu.suitetecsa.sdk.network.HttpResponse
import cu.suitetecsa.sdk.network.PortalCommunicator
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UserPortalBalanceHandlerTest {

    // Tests that top up balance with a valid recharge code is successful
    @Test
    fun test_top_up_balance_with_valid_recharge_code() {
        // Mock the dependencies
        val communicator = mock<PortalCommunicator>()
        val errorParser = mock<ErrorParser>()
        val tokenParser = mock<TokenParser>()

        // Create an instance of UserPortalBalanceHandler with the mocked dependencies
        val balanceHandler = UserPortalBalanceHandler.Builder()
            .withCommunicator(communicator)
            .withErrorParser(errorParser)
            .withTokenParser(tokenParser)
            .build()

        // Mock the necessary behavior of the dependencies
        whenever(
            communicator.performAction(any<TopUpBalance>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.success(Unit))
        whenever(
            communicator.performAction(any<String>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.success("csrf_token"))

        // Call the method under test
        val result = balanceHandler.topUpBalance("valid_recharge_code")

        assertTrue(result.isSuccess)
    }

    // Tests that transfer funds to another account with valid amount, password, and destination account is successful
    @Test
    fun test_transfer_funds_to_another_account_with_valid_parameters() {
        // Mock the dependencies
        val communicator = mock<PortalCommunicator>()
        val errorParser = mock<ErrorParser>()
        val tokenParser = mock<TokenParser>()

        // Create an instance of UserPortalBalanceHandler with the mocked dependencies
        val balanceHandler = UserPortalBalanceHandler.Builder()
            .withCommunicator(communicator)
            .withErrorParser(errorParser)
            .withTokenParser(tokenParser)
            .build()

        whenever(
            communicator.performAction(any<TransferFunds>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.success(Unit))
        whenever(
            communicator.performAction(any<String>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.success("csrf_token"))

        // Call the method under test
        val result = balanceHandler.transferFunds(100.0f, "valid_password", "valid_destination_account")

        // Verify assertions
        assertTrue(result.isSuccess)
    }

    // Tests that transfer funds to pay a quote of the Nauta Home service with valid amount and password is successful
    @Test
    fun test_transfer_funds_to_pay_nauta_home_service_with_valid_parameters() {
        // Mock the dependencies
        val communicator = mock<PortalCommunicator>()
        val errorParser = mock<ErrorParser>()
        val tokenParser = mock<TokenParser>()

        // Create an instance of UserPortalBalanceHandler with the mocked dependencies
        val balanceHandler = UserPortalBalanceHandler.Builder()
            .withCommunicator(communicator)
            .withErrorParser(errorParser)
            .withTokenParser(tokenParser)
            .build()

        // Mock the necessary behavior of the dependencies
        whenever(
            communicator.performAction(any<TransferFunds>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.success(Unit))
        whenever(
            communicator.performAction(any<String>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.success("csrf_token"))

        // Call the method under test
        val result = balanceHandler.transferFunds(100.0f, "valid_password")

        // Verify assertions
        assertTrue(result.isSuccess)
    }

    // Tests that top up balance with an invalid recharge code throws an exception
    @Test
    fun test_top_up_balance_with_invalid_recharge_code() {
        // Mock the dependencies
        val communicator = mock<PortalCommunicator>()
        val errorParser = mock<ErrorParser>()
        val tokenParser = mock<TokenParser>()

        // Create an instance of UserPortalBalanceHandler with the mocked dependencies
        val balanceHandler = UserPortalBalanceHandler.Builder()
            .withCommunicator(communicator)
            .withErrorParser(errorParser)
            .withTokenParser(tokenParser)
            .build()

        // Mock the necessary behavior of the dependencies
        whenever(
            communicator.performAction(any<TopUpBalance>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.failure(TopUpBalanceException("Invalid recharge code")))
        whenever(
            communicator.performAction(any<String>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.success("csrf_token"))

        // Call the method under test
        val result = balanceHandler.topUpBalance("invalid_recharge_code")

        // Verify assertions
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TopUpBalanceException)
    }

    // Tests that transfer funds with invalid amount, password, or destination account throws an exception
    @Test
    fun test_transfer_funds_with_invalid_parameters() {
        // Mock the dependencies
        val communicator = mock<PortalCommunicator>()
        val errorParser = mock<ErrorParser>()
        val tokenParser = mock<TokenParser>()

        // Create an instance of UserPortalBalanceHandler with the mocked dependencies
        val balanceHandler = UserPortalBalanceHandler.Builder()
            .withCommunicator(communicator)
            .withErrorParser(errorParser)
            .withTokenParser(tokenParser)
            .build()

        // Mock the necessary behavior of the dependencies
        whenever(
            communicator.performAction(any<TransferFunds>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.failure(TransferFundsException("Invalid parameters")))
        whenever(
            communicator.performAction(any<String>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.success("csrf_token"))

        // Call the method under test
        val result = balanceHandler.transferFunds(-100.0f, "invalid_password", "invalid_destination_account")

        // Verify assertions
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TransferFundsException)
    }

    // Tests that transfer funds with insufficient balance throws an exception
    @Test
    fun test_transfer_funds_with_insufficient_balance() {
        // Mock the dependencies
        val communicator = mock<PortalCommunicator>()
        val errorParser = mock<ErrorParser>()
        val tokenParser = mock<TokenParser>()

        // Create an instance of UserPortalBalanceHandler with the mocked dependencies
        val balanceHandler = UserPortalBalanceHandler.Builder()
            .withCommunicator(communicator)
            .withErrorParser(errorParser)
            .withTokenParser(tokenParser)
            .build()

        // Mock the necessary behavior of the dependencies
        whenever(
            communicator.performAction(any<TransferFunds>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.failure(TransferFundsException("Insufficient balance")))
        whenever(
            communicator.performAction(any<String>(), any<(HttpResponse) -> Any>())
        ).thenReturn(Result.success("csrf_token"))

        // Call the method under test
        val result = balanceHandler.transferFunds(1000.0f, "valid_password", "valid_destination_account")

        // Verify assertions
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TransferFundsException)
    }
}
