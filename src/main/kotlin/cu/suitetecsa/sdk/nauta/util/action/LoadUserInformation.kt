package cu.suitetecsa.sdk.nauta.util.action

import cu.suitetecsa.sdk.nauta.exception.NautaAttributeException
import cu.suitetecsa.sdk.nauta.util.PortalManager
import cu.suitetecsa.sdk.network.Action
import cu.suitetecsa.sdk.network.HttpMethod

internal data class LoadUserInformation(
    private val username: String? = null,
    private val password: String? = null,
    private val wlanUserIp: String? = null,
    private val csrfHw: String? = null,
    private val attributeUUID: String? = null,
    private val portal: PortalManager
) : Action {
    override val url: String = when (portal) {
        PortalManager.Connect -> "${portal.baseUrl}/EtecsaQueryServlet"
        PortalManager.User -> "${portal.baseUrl}/useraaa/user_info"
    }
    override val data: Map<String, String>? = when (portal) {
        PortalManager.Connect -> attributeUUID?.let {
            mapOf(
                "op" to "getLeftTime",
                "ATTRIBUTE_UUID" to it,
                "CSRFHW" to run { csrfHw ?: throw NautaAttributeException("csrfHw is required") },
                "wlanuserip" to run { wlanUserIp ?: throw NautaAttributeException("wlanUserIp is required") },
                "username" to run { username ?: throw NautaAttributeException("username is required") }
            )
        } ?: mapOf(
            "username" to run { username ?: throw NautaAttributeException("username is required") },
            "password" to run { password ?: throw NautaAttributeException("password is required") },
            "wlanuserip" to run { wlanUserIp ?: throw NautaAttributeException("wlanUserIp is required") },
            "CSRFHW" to run { csrfHw ?: throw NautaAttributeException("csrfHw is required") },
            "lang" to ""
        )

        PortalManager.User -> null
    }
    override val method: HttpMethod = HttpMethod.POST
}
