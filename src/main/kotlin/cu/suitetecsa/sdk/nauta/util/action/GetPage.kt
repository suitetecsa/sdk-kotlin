package cu.suitetecsa.sdk.nauta.util.action

import cu.suitetecsa.sdk.network.Action
import cu.suitetecsa.sdk.network.HttpMethod

internal data class GetPage(
    override val url: String,
    override val data: Map<String, String>? = null,
    override val method: HttpMethod = HttpMethod.GET
) : Action
