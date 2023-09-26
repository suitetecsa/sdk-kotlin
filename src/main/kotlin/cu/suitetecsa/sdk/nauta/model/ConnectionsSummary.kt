package cu.suitetecsa.sdk.nauta.model

data class ConnectionsSummary(
    override val count: Int,
    override val yearMonthSelected: String,
    val totalTime: Long,
    override val totalImport: Float,
    val uploaded: Double,
    val downloaded: Double,
    val totalTraffic: Double
) : Summary
