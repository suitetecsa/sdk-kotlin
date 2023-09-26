package cu.suitetecsa.sdk.nauta.util.action

import cu.suitetecsa.sdk.nauta.exception.NautaAttributeException
import cu.suitetecsa.sdk.nauta.util.PortalManager
import cu.suitetecsa.sdk.network.Action
import cu.suitetecsa.sdk.network.HttpMethod

internal data class Login(
    private val csrf: String? = null,
    private val wlanUserIp: String? = null,
    private val username: String,
    private val password: String,
    private val captchaCode: String = "",
    private val portal: PortalManager,
    override val method: HttpMethod = HttpMethod.POST
) : Action {
    override val url: String = when (portal) {
        PortalManager.Connect -> "${PortalManager.Connect.baseUrl}//LoginServlet"
        PortalManager.User -> "${PortalManager.User.baseUrl}/user/login/es-es"
    }
    override val data: Map<String, String> = when (portal) {
        PortalManager.Connect -> mapOf(
            "CSRFHW" to run { csrf ?: throw NautaAttributeException("CSRFHW is required") },
            "wlanuserip" to run { wlanUserIp ?: throw NautaAttributeException("wlanuserip is required") },
            "username" to username,
            "password" to password
        )

        PortalManager.User -> mapOf(
            "csrf" to run {
                csrf ?: if (method == HttpMethod.POST) throw NautaAttributeException("csrf is required") else ""
            },
            "login_user" to username,
            "password_user" to password,
            "captcha" to captchaCode,
            "btn_submit" to ""
        )
    }
}
