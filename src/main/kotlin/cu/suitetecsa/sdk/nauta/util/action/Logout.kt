package cu.suitetecsa.sdk.nauta.util.action

import cu.suitetecsa.sdk.nauta.util.PortalManager
import cu.suitetecsa.sdk.network.Action

internal data class Logout(
    private val username: String,
    private val wlanUserIp: String,
    private val csrfHw: String,
    private val attributeUUID: String
) : Action {
    override val url: String = "${PortalManager.Connect.baseUrl}/LogoutServlet"
    override val data: Map<String, String> = mutableMapOf(
        "username" to username,
        "wlanuserip" to wlanUserIp,
        "CSRFHW" to csrfHw,
        "ATTRIBUTE_UUID" to attributeUUID
    )
}
