package cu.suitetecsa.sdk.network

private const val TIMEOUT_MS = 30000

interface Action {
    val url: String
    val data: Map<String, String>?
        get() = null
    val method: HttpMethod
        get() = HttpMethod.GET
    val ignoreContentType: Boolean
        get() = false
    val timeout: Int
        get() = TIMEOUT_MS

    val csrfUrl: String?
        get() = null

    val count: Int
        get() = 0
    val yearMonthSelected: String?
        get() = null

    val pagesCount: Int
        get() = 0
    val large: Int
        get() = 0
    val reversed: Boolean
        get() = false
    val type: ActionType
        get() = ActionType.Connections
}
