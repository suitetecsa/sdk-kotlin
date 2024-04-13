package io.github.suitetecsa.sdk.network

import org.jetbrains.annotations.Contract
import java.nio.charset.StandardCharsets

class HttpResponse(content: ByteArray) {
    @get:Contract(value = " -> new", pure = true)
    val text: String
        get() = String(content, StandardCharsets.UTF_8)
    private val content: ByteArray = content.copyOf(content.size)
}
