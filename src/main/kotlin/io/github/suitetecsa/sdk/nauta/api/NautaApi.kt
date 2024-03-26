package io.github.suitetecsa.sdk.nauta.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.suitetecsa.sdk.nauta.model.login.LoginResponse
import io.github.suitetecsa.sdk.nauta.model.login.LoginResponseAdapter
import io.github.suitetecsa.sdk.nauta.model.users.UsersResponse
import io.github.suitetecsa.sdk.nauta.model.users.UsersResponseAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object NautaApi {
    private val moshi = Moshi.Builder()
        .add(LoginResponse::class.java, LoginResponseAdapter())
        .add(UsersResponse::class.java, UsersResponseAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.nauta.cu:5002")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    private val retrofitRX = Retrofit.Builder()
        .baseUrl("https://www.nauta.cu:5002")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    val portalAuthService: PortalAuthService by lazy {
        retrofit.create(PortalAuthService::class.java)
    }

    @JvmStatic
    val portalAuthServiceRX: PortalAuthServiceRx by lazy {
        retrofitRX.create(PortalAuthServiceRx::class.java)
    }
}
