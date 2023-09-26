package cu.suitetecsa.sdk.nauta.model

data class RechargesSummary(
    override val count: Int,
    override val yearMonthSelected: String,
    override val totalImport: Float
) : Summary
