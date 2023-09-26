package cu.suitetecsa.sdk.nauta.model

data class QuotesPaidSummary(
    override val count: Int,
    override val yearMonthSelected: String,
    override val totalImport: Float
) : Summary
