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

interface NautaService {
    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @GET("/captcha/captcha")
    suspend fun getCaptcha(): CaptchaResponse

    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/reset/validateUser")
    suspend fun validateUser(@Body request: ValidateUserRequest): NautaActionResponse


    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/reset/validateCodeUsuario")
    suspend fun validateConfirmCode(@Body request: ValidateCodeRequest): NautaActionResponse


    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/reset/resetPass")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): NautaActionResponse


    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/reset/registeruser")
    suspend fun registerUser(@Body request: RegisterUserRequest): NautaActionResponse


    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/reset/validateCodeIdentidad")
    suspend fun validateCodeIdentity(@Body request: ValidateCodeIdentityRequest): NautaActionResponse


    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/reset/createuser")
    suspend fun createUser(@Body request: CreateUserRequest): NautaActionResponse


    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse


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
