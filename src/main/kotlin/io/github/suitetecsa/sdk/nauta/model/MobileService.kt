package io.github.suitetecsa.sdk.nauta.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents a mobile service, encapsulating details about the user's mobile profile.
 *
 * @property profile The profile information of the mobile user, which includes
 * account details, balances, services, and other relevant data.
 */
@JsonClass(generateAdapter = true)
data class MobileService(
    @Json(name = "perfil") val profile: MobileProfile,
)
