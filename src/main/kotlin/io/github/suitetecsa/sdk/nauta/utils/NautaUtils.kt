package io.github.suitetecsa.sdk.nauta.utils

import com.auth0.jwt.JWT
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility object that provides methods for generating API keys and validating JWT tokens.
 * These methods are specifically designed to support authentication and token validation
 * in the context of secure communication with external services.
 */
object NautaUtils {
    /**
     * Generates and returns an API key string based on the current date and time.
     * The method constructs a string composed of predefined elements, computes its
     * SHA-512 hash, and encodes the result in Base64 format.
     *
     * @return A Base64-encoded string representing the generated API key in the format "ApiKey <hash>".
     */
    @JvmStatic
    fun createPasswordApp(): String {
        // Crea un objeto SimpleDateFormat para formatear la fecha actual
        val dateFormatter = SimpleDateFormat("ddMMyyyyHH")
        // Obtiene la fecha actual como una cadena en el formato "ddMMyyyyHH"
        val dateString = dateFormatter.format(Date())

        // Construye la cadena de clave de la aplicación
        val appKey = "portal" + dateString + "externalPortal"
        // Convierte la cadena de clave de la aplicación en un objeto ByteArray
        val appKeyData = appKey.toByteArray(Charsets.UTF_8)

        // Calcula el hash SHA-512 de la cadena de clave de la aplicación
        val hashedData = MessageDigest.getInstance("SHA-512").digest(appKeyData)
        // Codifica el hash en formato Base64 y devuelve la cadena resultante
        return "ApiKey ${Base64.getEncoder().encodeToString(hashedData)}"
    }

    /**
     * Verifies whether a given JWT token is still valid based on its expiration time.
     *
     * @param token A JWT token as a string to be validated.
     * @return True if the token's expiration time is in the future; false otherwise.
     */
    @JvmStatic
    fun isValidToken(token: String): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = JWT.decode(token).expiresAt
        return calendar.timeInMillis > Calendar.getInstance().timeInMillis
    }
}
