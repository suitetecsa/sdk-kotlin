package cu.suitetecsa.sdk.nauta.model

data class TransfersSummary(
    override val count: Int,
    override val yearMonthSelected: String,
    override val totalImport: Float
) : Summary
