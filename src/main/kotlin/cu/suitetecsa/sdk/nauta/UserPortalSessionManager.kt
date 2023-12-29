package cu.suitetecsa.sdk.nauta

import cu.suitetecsa.sdk.network.Action
import cu.suitetecsa.sdk.network.PortalCommunicator

interface UserPortalSessionManager {
    var sessionOwner: String?
    var isNautaHome: Boolean?
    val communicator: PortalCommunicator
    fun loadCsrf(action: Action): String
}
