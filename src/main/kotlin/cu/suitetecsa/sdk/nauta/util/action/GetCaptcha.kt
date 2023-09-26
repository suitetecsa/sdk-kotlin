package cu.suitetecsa.sdk.nauta.util.action

import cu.suitetecsa.sdk.nauta.util.PortalManager
import cu.suitetecsa.sdk.network.Action

private const val TIMEOUT_MS = 5000

internal object GetCaptcha : Action {
    override val url: String = "${PortalManager.User.baseUrl}/captcha/?"
    override val ignoreContentType: Boolean = true
    override val timeout: Int = TIMEOUT_MS
}
