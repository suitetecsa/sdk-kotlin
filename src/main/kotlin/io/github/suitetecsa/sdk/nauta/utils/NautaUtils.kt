package io.github.suitetecsa.sdk.nauta.utils

import com.auth0.jwt.JWT
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

object NautaUtils {
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

    @JvmStatic
    fun isValidToken(token: String): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = JWT.decode(token).expiresAt
        return calendar.timeInMillis > Calendar.getInstance().timeInMillis
    }
}
