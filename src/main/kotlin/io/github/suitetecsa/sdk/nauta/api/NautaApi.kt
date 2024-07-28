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

    val nautaService: NautaService by lazy {
        retrofit.create(NautaService::class.java)
    }

    @JvmStatic
    val nautaServiceRx: NautaServiceRx by lazy {
        retrofitRX.create(NautaServiceRx::class.java)
    }
}
