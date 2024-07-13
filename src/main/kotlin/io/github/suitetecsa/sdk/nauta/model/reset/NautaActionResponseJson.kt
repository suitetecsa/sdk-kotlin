package io.github.suitetecsa.sdk.nauta.model.reset

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class NautaActionResponseJson(val data: NautaActionResponse)
