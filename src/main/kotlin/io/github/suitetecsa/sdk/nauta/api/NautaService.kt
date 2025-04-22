package io.github.suitetecsa.sdk.nauta.api

import io.github.suitetecsa.sdk.nauta.model.captcha.CaptchaResponse
import io.github.suitetecsa.sdk.nauta.model.login.LoginRequest
import io.github.suitetecsa.sdk.nauta.model.login.LoginResponse
import io.github.suitetecsa.sdk.nauta.model.reset.CreateUserRequest
import io.github.suitetecsa.sdk.nauta.model.reset.NautaActionResponse
import io.github.suitetecsa.sdk.nauta.model.reset.RegisterUserRequest
import io.github.suitetecsa.sdk.nauta.model.reset.ResetPasswordRequest
import io.github.suitetecsa.sdk.nauta.model.reset.ValidateCodeIdentityRequest
import io.github.suitetecsa.sdk.nauta.model.reset.ValidateCodeRequest
import io.github.suitetecsa.sdk.nauta.model.reset.ValidateUserRequest
import io.github.suitetecsa.sdk.nauta.model.users.UsersRequest
import io.github.suitetecsa.sdk.nauta.model.users.UsersResponse
import io.github.suitetecsa.sdk.nauta.utils.NautaUtils
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Service interface for handling various Nauta-related operations.
 *
 * The `NautaService` interface defines a set of functions for interacting with the Nauta system,
 * including operations such as user authentication, registration, password reset, and
 * validating user information. These methods make HTTP requests to predefined endpoints
 * with structured request data and return specific response models.
 */
interface NautaService {
    /**
     * Retrieves a CAPTCHA response from the server.
     *
     * This method fetches a CAPTCHA response, which contains a request identifier and associated data.
     *
     * @return CaptchaResponse containing the unique request identifier and the CAPTCHA data.
     */
    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @GET("/captcha/captcha")
    suspend fun getCaptcha(): CaptchaResponse

    /**
     * Validates a user's identity using the provided request data.
     *
     * This method facilitates the validation of a user via an HTTP POST request,
     * leveraging a structured request payload containing necessary user information
     * such as captcha code, request identifier, and portal user details.
     *
     * @param request The information required for validating the user, encapsulated in a `ValidateUserRequest` object.
     * @return A `NautaActionResponse` containing the result and any additional details of the validation action.
     */
    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/reset/validateUser")
    suspend fun validateUser(@Body request: ValidateUserRequest): NautaActionResponse


    /**
     * Validates a confirmation code for a portal user.
     *
     * This method performs an HTTP POST request to validate the provided confirmation code
     * for the specified portal user. The request contains the user's identifier and the
     * confirmation code to be validated.
     *
     * @param request The information required to validate the confirmation code, encapsulated
     *                in a `ValidateCodeRequest` object. This includes the portal user's identifier
     *                and the code to be validated.
     * @return A `NautaActionResponse` object containing the result of the validation operation
     *         and any additional details.
     */
    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/reset/validateCodeUsuario")
    suspend fun validateConfirmCode(@Body request: ValidateCodeRequest): NautaActionResponse


    /**
     * Resets a user's password.
     *
     * This method sends a password reset request to the server, specifying the user account,
     * new password, and confirmation code required to authorize the operation.
     *
     * @param request The information required for resetting the password, encapsulated in
     *                a `ResetPasswordRequest` object, which includes the user account,
     *                new password, and confirmation code.
     * @return A `NautaActionResponse` containing the result of the password reset operation
     *         and any additional details provided by the server.
     */
    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/reset/resetPass")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): NautaActionResponse


    /**
     * Registers a user with the provided information.
     *
     * This method sends a user registration request to the server using the provided
     * information in the `RegisterUserRequest` object. The request typically contains
     * details such as the user's captcha code, request identifier, national ID, and
     * phone number.
     *
     * @param request An instance of `RegisterUserRequest` containing the required
     *                user registration details such as captcha code, request ID,
     *                national identification number, and phone number.
     * @return A `NautaActionResponse` representing the result of the registration
     *         operation, along with any additional details provided by the server.
     */
    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/reset/registeruser")
    suspend fun registerUser(@Body request: RegisterUserRequest): NautaActionResponse


    /**
     * Validates a user's identity using a confirmation code and their national ID.
     *
     * This method sends an HTTP POST request to validate the provided identity confirmation
     * code for the specified user, using the information encapsulated in the
     * `ValidateCodeIdentityRequest` object.
     *
     * @param request The request data required to validate the user's identity, encapsulated
     *                in a `ValidateCodeIdentityRequest` object. This includes the
     *                user's national ID (`dni`) and the confirmation code (`confirmCode`).
     * @return A `NautaActionResponse` containing the result of the identity validation
     *         operation and any additional details provided by the server.
     */
    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/reset/validateCodeIdentidad")
    suspend fun validateCodeIdentity(@Body request: ValidateCodeIdentityRequest): NautaActionResponse


    /**
     * Creates a new user with the provided information.
     *
     * This method sends a request to the server to create a new user account. The request contains
     * the required user details such as phone number, password, and national ID.
     *
     * @param request An instance of `CreateUserRequest` containing the user's phone number,
     *                password, and national identification number.
     * @return A `NautaActionResponse` representing the result of the user creation operation,
     *         including any additional details provided by the server.
     */
    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/reset/createuser")
    suspend fun createUser(@Body request: CreateUserRequest): NautaActionResponse


    /**
     * Authenticates a user with the provided login credentials.
     *
     * This method sends a login request to the server with the necessary information,
     * such as username, password, account type, a unique request identifier, and a CAPTCHA code.
     *
     * @param request The details required for login, encapsulated in a `LoginRequest` object.
     *                This includes the username, password, account type, request identifier,
     *                and CAPTCHA code.
     * @return A `LoginResponse` object containing the result of the login operation,
     *         including an authentication token, user details, and the outcome of the request.
     */
    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse


    /**
     * Sends a request to retrieve or manage user-related information.
     *
     * This method performs an HTTP POST request to the `/users` endpoint with the provided
     * authorization token, user request details, and an application password. The response
     * contains user-specific data relevant to the request.
     *
     * @param authorization The authorization token required for the request, provided via the `Authorization` header.
     * @param request The user-related data encapsulated in a `UsersRequest` object.
     * @param passwordApp The generated application password, provided via the `passwordApp` header. Defaults to a
     * value generated by `NautaUtils.createPasswordApp()`.
     * @return A `UsersResponse` object containing the result of the user-related operation.
     */
    @Headers(
        "User-Agent: SuitETECSA/1.0.0",
        "Content-Type: application/json",
        "usernameApp: portal",
    )
    @POST("/users")
    suspend fun users(
        @Header("Authorization") authorization: String,
        @Body request: UsersRequest,
        @Header("passwordApp") passwordApp: String = NautaUtils.createPasswordApp()
    ): UsersResponse
}
