package cu.suitetecsa.sdk.nauta.util.action

import cu.suitetecsa.sdk.nauta.exception.NautaAttributeException
import cu.suitetecsa.sdk.nauta.util.PortalManager
import cu.suitetecsa.sdk.network.Action
import cu.suitetecsa.sdk.network.HttpMethod

internal data class TransferFunds(
    private val csrf: String? = null,
    private val amount: Float,
    private val password: String,
    private val destinationAccount: String? = null,
    override val method: HttpMethod = HttpMethod.POST
) : Action {
    override val url: String = "${PortalManager.User.baseUrl}${destinationAccount
        ?.let { "/useraaa/transfer_balance" } ?: "/useraaa/transfer_nautahogarpaid"}"
    override val data: Map<String, String> = destinationAccount?.let {
        mutableMapOf(
            "csrf" to run {
                csrf ?: if (method == HttpMethod.POST) throw NautaAttributeException("csrf is required") else ""
            },
            "transfer" to "%.2f".format(amount).replace(".", ","),
            "id_cuenta" to it,
            "password_user" to password,
            "action" to "checkdata"
        )
    } ?: mutableMapOf(
        "csrf" to run {
            csrf ?: if (method == HttpMethod.POST) throw NautaAttributeException("csrf is required") else ""
        },
        "transfer" to "%.2f".format(amount).replace(".", ","),
        "password_User" to password,
        "action" to "checkdata"
    )
}
