package cu.suitetecsa.sdk.nauta.util.action

import cu.suitetecsa.sdk.nauta.util.PortalManager
import cu.suitetecsa.sdk.network.Action
import cu.suitetecsa.sdk.network.HttpMethod

internal data class ChangePassword(
    private val csrf: String? = null,
    private val oldPassword: String,
    private val newPassword: String,
    private val changeMail: Boolean = false
) : Action {
    override val url: String =
        "${PortalManager.User.baseUrl}${if (changeMail) "/mail/change_password" else "/Useraaa/change_password"}"
    override val data: Map<String, String> = mutableMapOf(
        "csrf" to run { csrf ?: "" },
        "old_password" to oldPassword,
        "new_password" to newPassword,
        "repeat_new_password" to newPassword,
        "btn_submit" to ""
    )
    override val method: HttpMethod = HttpMethod.POST
}
