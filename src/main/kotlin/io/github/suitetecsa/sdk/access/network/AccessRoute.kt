package io.github.suitetecsa.sdk.access.network

import io.github.suitetecsa.sdk.network.HttpMethod

sealed class AccessRoute(
    val url: String,
    val data: Map<String, String>? = null,
    val method: HttpMethod = HttpMethod.GET,
    val timeout: Int = 30000
) {
    data object CheckAccess : AccessRoute("http://www.cubadebate.cu/")
    data object Init : AccessRoute("https://secure.etecsa.net:8443/")
    data class UserInfo(
        val username: String,
        val password: String,
        val wlanUserIp: String,
        val csrfHw: String,
    ) : AccessRoute(
        url = "https://secure.etecsa.net:8443/EtecsaQueryServlet",
        data = mapOf(
            "username" to username,
            "password" to password,
            "wlanuserip" to wlanUserIp,
            "CSRFHW" to csrfHw,
            "lang" to ""
        ),
        method = HttpMethod.POST
    )
    data class GetTime(
        val username: String,
        val wlanUserIp: String,
        val csrfHw: String,
        val attributeUUID: String,
    ) : AccessRoute(
        url = "https://secure.etecsa.net:8443/EtecsaQueryServlet",
        data = mapOf(
            "op" to "getLeftTime",
            "ATTRIBUTE_UUID" to attributeUUID,
            "CSRFHW" to csrfHw,
            "wlanuserip" to wlanUserIp,
            "username" to username
        ),
        method = HttpMethod.POST
    )

    data class Connect(
        val username: String,
        val password: String,
        val csrfHw: String,
        val wlanUserIp: String,
    ) : AccessRoute(
        url = "https://secure.etecsa.net:8443//LoginServlet",
        data = mapOf(
            "CSRFHW" to csrfHw,
            "wlanuserip" to wlanUserIp,
            "username" to username,
            "password" to password
        ),
        method = HttpMethod.POST
    )

    data class Disconnect(
        val username: String,
        val wlanUserIp: String,
        val csrfHw: String,
        val attributeUUID: String
    ) : AccessRoute(
        url = "https://secure.etecsa.net:8443//LoginServlet?" +
            "username=$username&wlanuserip=$wlanUserIp&CSRFHW=$csrfHw&ATTRIBUTE_UUID=$attributeUUID"
    )
}
