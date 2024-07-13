package io.github.suitetecsa.sdk.nauta.api;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;
import io.github.suitetecsa.sdk.nauta.model.User;
import io.github.suitetecsa.sdk.nauta.model.captcha.CaptchaResponse;
import io.github.suitetecsa.sdk.nauta.model.login.LoginRequest;
import io.github.suitetecsa.sdk.nauta.model.login.LoginResponse;
import io.github.suitetecsa.sdk.nauta.model.login.LoginResponseAdapter;
import io.github.suitetecsa.sdk.nauta.model.users.UsersRequest;
import io.github.suitetecsa.sdk.nauta.model.users.UsersResponse;
import io.github.suitetecsa.sdk.nauta.model.users.UsersResponseAdapter;
import io.github.suitetecsa.sdk.nauta.utils.NautaUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.BufferedSource;
import okio.Okio;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@RunWith(JUnit4.class)
public class NautaServiceRxTest {
    private MockWebServer mockWebServer;
    private NautaServiceRx service;

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        Moshi moshi = new Moshi.Builder()
                .add(LoginResponse.class, new LoginResponseAdapter())
                .add(UsersResponse.class, new UsersResponseAdapter())
                .addLast(new KotlinJsonAdapterFactory())
                .build();

        service = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(NautaServiceRx.class);
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void testGetCaptchaOk() throws IOException, InterruptedException {
        enqueueResponse("captcha.json");
        CaptchaResponse data = service.getCaptcha().blockingGet();
        RecordedRequest request = mockWebServer.takeRequest();

        Assert.assertEquals(request.getMethod(), "GET");
        Assert.assertEquals(request.getPath(), "/captcha/captcha?");
        Assert.assertEquals(request.getHeader("User-Agent"), "SuitETECSA/1.0.0");
        Assert.assertEquals(request.getHeader("Content-Type"), "application/json");

        Assert.assertEquals(data.getIdRequest(), "e411473c64b6a7916fe96ef8e6b73e46f2010675");
    }

    @Test
    public void testLoginOk() throws IOException, InterruptedException {
        enqueueResponse("login.json");
        LoginRequest loginRequest = new LoginRequest(
                "username",
                "password",
                "USUARIO_PORTAL",
                "e411473c64b6a7916fe96ef8e6b73e46f2010675",
                "HSPK"
        );
        LoginResponse data = service.login(loginRequest).blockingGet();
        RecordedRequest request = mockWebServer.takeRequest();

        Assert.assertEquals(request.getMethod(), "POST");
        Assert.assertEquals(request.getPath(), "/login");
        Assert.assertEquals(request.getHeader("User-Agent"), "SuitETECSA/1.0.0");
        Assert.assertEquals(request.getHeader("Content-Type"), "application/json");

        Assert.assertEquals(((User) data.getUser()).getClient().getName(), "Pepito Peres");
        Assert.assertEquals(((User) data.getUser()).getClient().getPhoneNumber(), "51234567");
        Assert.assertEquals(((User) data.getUser()).getClient().getEmail(), "pepito@gmail.com");
        Assert.assertEquals(((User) data.getUser()).getClient().getOperations().get(0).getUrl(), "queryPagosOnLine");

        Assert.assertEquals(((User) data.getUser()).getServices().getNavServices().get(0).getProductType(), "NAVEGACION");
        Assert.assertEquals(((User) data.getUser()).getServices().getNavServices().get(0).getProfile().getAccessType(), "NAUTA_INTERNACIONAL_RECARGABLE");
        Assert.assertEquals(((User) data.getUser()).getServices().getMobileServices().get(0).getProfile().getId(), "5351234567");
        Assert.assertEquals(((User) data.getUser()).getServices().getMobileServices().size(), 3);
        Assert.assertEquals(((User) data.getUser()).getServices().getMailServices().size(), 1);
        Assert.assertEquals(((User) data.getUser()).getServices().getMailServices().get(0).getOperations().size(), 2);
        Assert.assertEquals(((User) data.getUser()).getServices().getNavServices().size(), 1);
        Assert.assertEquals(((User) data.getUser()).getServices().getNavServices().get(0).getOperations().size(), 9);
    }

    @Test
    public void testGetUsersNotUpdated() throws IOException, InterruptedException {
        enqueueResponse("users_not_updated.json");
        UsersRequest usersRequest = new UsersRequest("username", "password");
        String passwordApp = NautaUtils.createPasswordApp();
        UsersResponse data = service.users("Bearer Token", usersRequest, passwordApp).blockingGet();
        RecordedRequest request = mockWebServer.takeRequest();

        Assert.assertEquals(request.getMethod(), "POST");
        Assert.assertEquals(request.getPath(), "/users");
        Assert.assertEquals(request.getHeader("User-Agent"), "SuitETECSA/1.0.0");
        Assert.assertEquals(request.getHeader("Content-Type"), "application/json");
        Assert.assertEquals(request.getHeader("usernameApp"), "portal");
        Assert.assertEquals(request.getHeader("Authorization"), "Bearer Token");
        Assert.assertEquals(request.getHeader("passwordApp"), passwordApp);

        Assert.assertTrue(data.getUser() instanceof User);
        Assert.assertEquals(data.getResult(), "ok");
        Assert.assertEquals(((User) data.getUser()).getUpdatedServices(), "false");
        Assert.assertEquals(((User) data.getUser()).getServices().getMobileServices().size(), 0);
    }

    private void enqueueResponse(String fileName) throws IOException {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("api-response/" + fileName);

        assert inputStream != null;
        BufferedSource source = Okio.buffer(Okio.source(inputStream));

        String body = source.readString(StandardCharsets.UTF_8);

        MockResponse mockResponse = new MockResponse()
                .setBody(body);

        mockWebServer.enqueue(mockResponse);
    }

}
