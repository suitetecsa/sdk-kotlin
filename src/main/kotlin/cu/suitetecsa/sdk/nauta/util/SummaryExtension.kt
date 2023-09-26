package cu.suitetecsa.sdk.nauta.util

import cu.suitetecsa.sdk.nauta.model.Summary
import kotlin.math.ceil

private const val PAGE_MAX_LENGTH = 14

val <T : Summary> T.pagesCount: Int
    get() = ceil(this.count.toDouble() / PAGE_MAX_LENGTH).toInt()
