package io.github.suitetecsa.sdk.nauta.model.reset

/**
 * Represents a parameter to be included in a Nauta action request.
 *
 * This model is used to encapsulate key-value pairs required for requests
 * to the Nauta service. The `name` property specifies the parameter's key,
 * while the `value` property contains its corresponding value.
 */
internal data class NautaActionRequestParam(val name: String, val value: String)
