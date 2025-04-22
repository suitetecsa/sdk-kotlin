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

/**
 * Provides a utility for creating a customized OkHttpClient instance
 * configured with a specific SSL/TLS context and custom trust manager.
 *
 * This class is intended to load a specific keystore from resources,
 * configure a TrustManager with the keystore, and create an OkHttpClient
 * instance that uses the established SSL context for secure communication.
 *
 * Functions:
 * - `getCustomTrustClient`: Constructs and returns a configured OkHttpClient
 *   instance using a custom SSL context and trust manager.
 *
 * Exceptions:
 * - `KeystoreNotFoundException`: Thrown if the keystore file cannot be found
 *   in the application resources.
 * - `KeystoreLoadingException`: Thrown if an error occurs while loading the
 *   keystore file.
 * - `SSLContextInitializationException`: Thrown if an error happens during
 *   the initialization of the SSL context.
 */
object CustomTrustManager {
    /**
     * Creates and returns a custom OkHttpClient instance configured with a specific trust store.
     * The method loads a custom BKS keystore from resources, initializes an SSLContext with the keystore,
     * and configures the OkHttpClient's SSL socket factory with the configured SSLContext and trust managers.
     *
     * @return OkHttpClient instance configured with the custom trust store for secure connections.
     * @throws KeystoreNotFoundException if the keystore file is not found in the resources.
     * @throws KeystoreLoadingException if there is an error while loading the keystore.
     * @throws SSLContextInitializationException if there is an error initializing the SSLContext.
     */
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
