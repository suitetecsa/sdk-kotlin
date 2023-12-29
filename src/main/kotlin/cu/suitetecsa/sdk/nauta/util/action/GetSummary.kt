package cu.suitetecsa.sdk.nauta.util.action

import cu.suitetecsa.sdk.network.Action
import cu.suitetecsa.sdk.network.ActionType
import cu.suitetecsa.sdk.network.HttpMethod

internal data class GetSummary(
    private val csrf: String? = null,
    private val year: Int,
    private val month: Int,
    override val type: ActionType,
    override val method: HttpMethod = HttpMethod.POST
) : Action {
    override val url: String = when (type) {
        ActionType.Connections -> "/useraaa/service_detail_summary/"
        ActionType.Recharges -> "/useraaa/recharge_detail_summary/"
        ActionType.Transfers -> "/useraaa/transfer_detail_summary/"
        ActionType.QuotesPaid -> "/useraaa/nautahogarpaid_detail_summary/"
    }
    override val data: Map<String, String> = mutableMapOf(
        "csrf" to run { csrf ?: "" },
        "year_month" to "$year-${"%02d".format(month)}",
        "list_type" to when (type) {
            ActionType.Connections -> "service_detail"
            ActionType.Recharges -> "recharge_detail"
            ActionType.Transfers -> "transfer_detail"
            ActionType.QuotesPaid -> "nautahogarpaid_detail"
        }
    )
    override val csrfUrl: String = when (type) {
        ActionType.Connections -> "/useraaa/service_detail/"
        ActionType.Recharges -> "/useraaa/recharge_detail/"
        ActionType.Transfers -> "/useraaa/transfer_detail/"
        ActionType.QuotesPaid -> "/useraaa/nautahogarpaid_detail/"
    }
}
