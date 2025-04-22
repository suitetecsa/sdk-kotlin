package io.github.suitetecsa.sdk.nauta.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.suitetecsa.sdk.nauta.model.login.LoginResponse
import io.github.suitetecsa.sdk.nauta.model.login.LoginResponseAdapter
import io.github.suitetecsa.sdk.nauta.model.reset.CreateUserRequest
import io.github.suitetecsa.sdk.nauta.model.reset.CreateUserRequestAdapter
import io.github.suitetecsa.sdk.nauta.model.reset.NautaActionResponse
import io.github.suitetecsa.sdk.nauta.model.reset.NautaActionResponseAdapter
import io.github.suitetecsa.sdk.nauta.model.reset.RegisterUserRequest
import io.github.suitetecsa.sdk.nauta.model.reset.RegisterUserRequestAdapter
import io.github.suitetecsa.sdk.nauta.model.reset.ResetPasswordRequest
import io.github.suitetecsa.sdk.nauta.model.reset.ResetPasswordRequestAdapter
import io.github.suitetecsa.sdk.nauta.model.reset.ValidateCodeIdentityRequest
import io.github.suitetecsa.sdk.nauta.model.reset.ValidateCodeIdentityRequestAdapter
import io.github.suitetecsa.sdk.nauta.model.reset.ValidateCodeRequest
import io.github.suitetecsa.sdk.nauta.model.reset.ValidateCodeRequestAdapter
import io.github.suitetecsa.sdk.nauta.model.reset.ValidateUserRequest
import io.github.suitetecsa.sdk.nauta.model.reset.ValidateUserRequestAdapter
import io.github.suitetecsa.sdk.nauta.model.users.UsersResponse
import io.github.suitetecsa.sdk.nauta.model.users.UsersResponseAdapter
import io.github.suitetecsa.sdk.nauta.utils.CustomTrustManager
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Provides an entry point for interacting with the Nauta system through predefined services.
 *
 * This singleton object encapsulates the setup and configuration required to connect to the Nauta
 * API. It uses Retrofit and Moshi for HTTP communication and JSON serialization/deserialization,
 * and supports both suspend functions and reactive programming with RxJava3.
 *
 * The object defines:
 * - A lazily initialized `NautaService` for standard operations.
 * - A lazily initialized `NautaServiceRx` for reactive operations.
 *
 * It includes custom `JsonAdapter`s to serialize and deserialize specific request and response
 * objects in the format expected by the Nauta system API.
 *
 * The Nauta API is commonly used for operations like user authentication, registration, identity
 * and code validation, and password resets.
 */
object NautaApi {
    private val moshi = Moshi.Builder()
        .add(ResetPasswordRequest::class.java, ResetPasswordRequestAdapter())
        .add(ValidateCodeRequest::class.java, ValidateCodeRequestAdapter())
        .add(ValidateUserRequest::class.java, ValidateUserRequestAdapter())
        .add(RegisterUserRequest::class.java, RegisterUserRequestAdapter())
        .add(CreateUserRequest::class.java, CreateUserRequestAdapter())
        .add(ValidateCodeIdentityRequest::class.java, ValidateCodeIdentityRequestAdapter())
        .add(LoginResponse::class.java, LoginResponseAdapter())
        .add(UsersResponse::class.java, UsersResponseAdapter())
        .add(NautaActionResponse::class.java, NautaActionResponseAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.nauta.cu:5002")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(CustomTrustManager.getCustomTrustClient())
        .build()
    private val retrofitRX = Retrofit.Builder()
        .baseUrl("https://www.nauta.cu:5002")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(CustomTrustManager.getCustomTrustClient())
        .build()

    /**
     * Lazily initialized instance of `NautaService` for performing Nauta-related operations.
     *
     * This variable uses Retrofit to create an implementation of the `NautaService` interface,
     * which defines various HTTP-based operations such as user authentication, registration,
     * password management, and identity validation. The service provides abstractions for
     * communicating with the Nauta system by mapping API endpoints to Kotlin suspend functions.
     */
    val nautaService: NautaService by lazy {
        retrofit.create(NautaService::class.java)
    }

    /**
     * A lazily initialized singleton that provides an instance of the NautaServiceRx interface.
     *
     * This variable facilitates interaction with the Nauta service via reactive HTTP operations.
     * The interface, defined by `NautaServiceRx`, includes functions such as retrieving a CAPTCHA,
     * performing user login, and managing user data. All methods in the service return reactive
     * types suitable for use with RxJava.
     *
     * This instance is created using a Retrofit configuration and is intended for use wherever
     * the Nauta service needs to be accessed.
     */
    @JvmStatic
    val nautaServiceRx: NautaServiceRx by lazy {
        retrofitRX.create(NautaServiceRx::class.java)
    }
}
