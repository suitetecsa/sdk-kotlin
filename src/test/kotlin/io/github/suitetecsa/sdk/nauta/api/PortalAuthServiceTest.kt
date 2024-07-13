package io.github.suitetecsa.sdk.nauta.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.suitetecsa.sdk.nauta.model.User
import io.github.suitetecsa.sdk.nauta.model.login.LoginRequest
import io.github.suitetecsa.sdk.nauta.model.login.LoginResponse
import io.github.suitetecsa.sdk.nauta.model.login.LoginResponseAdapter
import io.github.suitetecsa.sdk.nauta.model.users.UsersRequest
import io.github.suitetecsa.sdk.nauta.model.users.UsersResponse
import io.github.suitetecsa.sdk.nauta.model.users.UsersResponseAdapter
import io.github.suitetecsa.sdk.nauta.utils.NautaUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertTrue

@RunWith(JUnit4::class)
class PortalAuthServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var service: NautaService

    @BeforeTest
    fun createService() {
        mockWebServer = MockWebServer()

        val moshi = Moshi.Builder()
            .add(LoginResponse::class.java, LoginResponseAdapter())
            .add(UsersResponse::class.java, UsersResponseAdapter())
            .addLast(KotlinJsonAdapterFactory())
            .build()

        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(NautaService::class.java)
    }

    @AfterTest
    fun stopService() {
        mockWebServer.shutdown()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test get captcha Ok`() = runTest {
        enqueueResponse("captcha.json")
        val data = service.getCaptcha()
        val request = mockWebServer.takeRequest()

        MatcherAssert.assertThat(request.method, CoreMatchers.`is`("GET"))
        MatcherAssert.assertThat(request.path, CoreMatchers.`is`("/captcha/captcha?"))
        MatcherAssert.assertThat(request.headers["User-Agent"], CoreMatchers.`is`("SuitETECSA/1.0.0"))
        MatcherAssert.assertThat(request.headers["Content-Type"], CoreMatchers.`is`("application/json"))

        MatcherAssert.assertThat(data.idRequest, CoreMatchers.`is`("e411473c64b6a7916fe96ef8e6b73e46f2010675"))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test login Ok`() = runTest {
        enqueueResponse("login.json")
        val data = service.login(
            LoginRequest(
                username = "username",
                password = "password",
                idRequest = "e411473c64b6a7916fe96ef8e6b73e46f2010675",
                captchaCode = "HSPK"
            )
        )
        val request = mockWebServer.takeRequest()

        MatcherAssert.assertThat(request.method, CoreMatchers.`is`("POST"))
        MatcherAssert.assertThat(request.path, CoreMatchers.`is`("/login"))
        MatcherAssert.assertThat(request.headers["User-Agent"], CoreMatchers.`is`("SuitETECSA/1.0.0"))
        MatcherAssert.assertThat(request.headers["Content-Type"], CoreMatchers.`is`("application/json"))

        MatcherAssert.assertThat((data.user as User).client.name, CoreMatchers.`is`("Pepito Peres"))
        MatcherAssert.assertThat((data.user as User).client.phoneNumber, CoreMatchers.`is`("51234567"))
        MatcherAssert.assertThat((data.user as User).client.email, CoreMatchers.`is`("pepito@gmail.com"))
        MatcherAssert.assertThat((data.user as User).client.operations[0].url, CoreMatchers.`is`("queryPagosOnLine"))
        MatcherAssert.assertThat(
            (data.user as User).services.navServices[0].productType,
            CoreMatchers.`is`("NAVEGACION")
        )
        MatcherAssert.assertThat(
            (data.user as User).services.navServices[0].profile.accessType,
            CoreMatchers.`is`("NAUTA_INTERNACIONAL_RECARGABLE")
        )
        MatcherAssert.assertThat(
            (data.user as User).services.mobileServices[0].profile.id,
            CoreMatchers.`is`("5351234567")
        )
        MatcherAssert.assertThat(
            (data.user as User).services.mobileServices.size,
            CoreMatchers.`is`(3)
        )
        MatcherAssert.assertThat(
            (data.user as User).services.mailServices.size,
            CoreMatchers.`is`(1)
        )
        MatcherAssert.assertThat(
            (data.user as User).services.mailServices.first().operations.size,
            CoreMatchers.`is`(2)
        )
        MatcherAssert.assertThat(
            (data.user as User).services.navServices.size,
            CoreMatchers.`is`(1)
        )
        MatcherAssert.assertThat(
            (data.user as User).services.navServices.first().operations.size,
            CoreMatchers.`is`(9)
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test get users not updated`() = runTest {
        enqueueResponse("users_not_updated.json")
        val passwordApp = NautaUtils.createPasswordApp()
        val data = service.users("Bearer Token", UsersRequest("", ""), passwordApp)
        val request = mockWebServer.takeRequest()

        MatcherAssert.assertThat(request.method, CoreMatchers.`is`("POST"))
        MatcherAssert.assertThat(request.path, CoreMatchers.`is`("/users"))
        MatcherAssert.assertThat(request.headers["User-Agent"], CoreMatchers.`is`("SuitETECSA/1.0.0"))
        MatcherAssert.assertThat(request.headers["Content-Type"], CoreMatchers.`is`("application/json"))
        MatcherAssert.assertThat(request.headers["usernameApp"], CoreMatchers.`is`("portal"))
        MatcherAssert.assertThat(request.headers["Authorization"], CoreMatchers.`is`("Bearer Token"))
        MatcherAssert.assertThat(request.headers["passwordApp"], CoreMatchers.`is`(passwordApp))

        assertTrue(data.user is User)
        MatcherAssert.assertThat(data.result, CoreMatchers.`is`("ok"))
        MatcherAssert.assertThat((data.user as User).updatedServices, CoreMatchers.`is`("false"))
        MatcherAssert.assertThat((data.user as User).services.mobileServices.size, CoreMatchers.`is`(0))
    }

    private fun enqueueResponse(fileName: String) {
        val inputString = javaClass.classLoader?.getResourceAsStream("api-response/$fileName")?.source()?.buffer()
        mockWebServer.enqueue(MockResponse().setBody(inputString!!.readString(Charsets.UTF_8)))
    }
}
