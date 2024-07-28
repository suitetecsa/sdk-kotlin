package io.github.suitetecsa.sdk.nauta.utils

import io.github.suitetecsa.sdk.exception.KeystoreLoadingException
import io.github.suitetecsa.sdk.exception.KeystoreNotFoundException
import io.github.suitetecsa.sdk.exception.SSLContextInitializationException
import okhttp3.OkHttpClient
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyStore
import java.security.Security
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object CustomTrustManager {
    @JvmStatic
    fun getCustomTrustClient(): OkHttpClient = runCatching {
        // Registrar el proveedor de Bouncy Castle
        Security.addProvider(BouncyCastleProvider())
        // Carga el archivo .bks desde los recursos
        val keyStoreStream = this::class.java.classLoader.getResourceAsStream("my_keystore.bks")
            ?: throw KeystoreNotFoundException("Keystore not found in resources")

        // Crea un KeyStore de tipo BKS
        val keyStore = KeyStore.getInstance("BKS")
        keyStore.load(keyStoreStream, "MyPassword".toCharArray())

        // Crea un TrustManagerFactory con el KeyStore
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)

        // Crea un SSLContext con el TrustManagerFactory
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustManagerFactory.trustManagers, null)

        // Cierra el InputStream
        keyStoreStream.close()

        // Crea un OkHttpClient usando el SSLContext
        OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManagerFactory.trustManagers[0] as X509TrustManager)
            .build()

    }.onFailure {
        if (it is KeystoreNotFoundException)
            throw KeystoreLoadingException("Failed to load the keystore", it)
        else
            throw SSLContextInitializationException("Failed to initialize SSL context", it)
    }.getOrThrow()
}
