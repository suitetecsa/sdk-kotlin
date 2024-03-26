package io.github.suitetecsa.sdk.exception

class NautaException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, throwable: Throwable?) : super(message, throwable)
}
