package cu.suitetecsa.sdk.nauta.util.action

import cu.suitetecsa.sdk.nauta.exception.NautaAttributeException
import cu.suitetecsa.sdk.nauta.util.PortalManager
import cu.suitetecsa.sdk.network.Action
import cu.suitetecsa.sdk.network.HttpMethod

internal data class TopUpBalance(
    private val csrf: String? = null,
    private val rechargeCode: String,
    override val method: HttpMethod = HttpMethod.POST
) : Action {
    override val url: String = "${PortalManager.User.baseUrl}/useraaa/recharge_account"
    override val data: Map<String, String> = mutableMapOf(
        "csrf" to run {
            csrf ?: if (method == HttpMethod.POST) throw NautaAttributeException("csrf is required") else ""
        },
        "recharge_code" to rechargeCode,
        "btn_submit" to ""
    )
}
