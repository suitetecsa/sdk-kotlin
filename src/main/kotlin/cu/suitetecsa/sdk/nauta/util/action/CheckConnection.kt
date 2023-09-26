package cu.suitetecsa.sdk.nauta.util.action

import cu.suitetecsa.sdk.network.Action

internal data class CheckConnection(private val checkUrl: String? = null) : Action {
    override val url: String = checkUrl ?: "http://www.cubadebate.cu/"
}
