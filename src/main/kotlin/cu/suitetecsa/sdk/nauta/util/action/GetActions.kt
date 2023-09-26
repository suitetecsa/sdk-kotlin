package cu.suitetecsa.sdk.nauta.util.action

import cu.suitetecsa.sdk.nauta.util.PortalManager
import cu.suitetecsa.sdk.network.Action
import cu.suitetecsa.sdk.network.ActionType

internal data class GetActions(
    override val count: Int,
    override val yearMonthSelected: String,
    override val pagesCount: Int,
    override val reversed: Boolean,
    override val type: ActionType
) : Action {
    override val url: String = PortalManager.User.baseUrl + when (type) {
        ActionType.Connections -> "/useraaa/service_detail_list/"
        ActionType.Recharges -> "/useraaa/recharge_detail_list/"
        ActionType.Transfers -> "/useraaa/transfer_detail_list/"
        ActionType.QuotesPaid -> "/useraaa/nautahogarpaid_detail_list/"
    }
}
