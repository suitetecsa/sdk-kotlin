package io.github.suitetecsa.sdk.nauta.api

import io.github.suitetecsa.sdk.nauta.model.captcha.CaptchaResponse
import io.github.suitetecsa.sdk.nauta.model.login.LoginRequest
import io.github.suitetecsa.sdk.nauta.model.login.LoginResponse
import io.github.suitetecsa.sdk.nauta.model.users.UsersRequest
import io.github.suitetecsa.sdk.nauta.model.users.UsersResponse
import io.github.suitetecsa.sdk.nauta.utils.NautaUtils
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface PortalAuthService {
    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @GET("/captcha/captcha?")
    suspend fun getCaptcha(): CaptchaResponse

    @Headers("User-Agent: SuitETECSA/1.0.0", "Content-Type: application/json")
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse


    @Headers(
        "User-Agent: SuitETECSA/1.0.0",
        "Content-Type: application/json",
        "usernameApp: portal",
    )
    @POST("/users")
    suspend fun users(
        @Header("Authorization") authorization: String,
        @Body userRequest: UsersRequest,
        @Header("passwordApp") passwordApp: String = NautaUtils.createPasswordApp()
    ): UsersResponse
}
