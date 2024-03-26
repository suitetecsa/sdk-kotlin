package io.github.suitetecsa.sdk.access.utils

/**
 * Clase que maneja excepciones y crea instancias de excepciones utilizando una factoría de excepciones.
 *
 */
class ExceptionHandler<T : Throwable>(private val exceptionFactory: ExceptionFactory<T>) {
    /**
     * Maneja una excepción y crea una instancia de excepción utilizando la factoría de excepciones y los
     * mensajes de error dados.
     *
     * @param message El mensaje de la excepción.
     * @param errors Los mensajes de error adicionales (opcional).
     * @return Una instancia de excepción creada.
     */
    fun handleException(message: String, errors: List<String>): T {
        val errorMessage = if (errors.isEmpty()) "No specific error message" else java.lang.String.join("; ", errors)
        return exceptionFactory.createException("$message :: $errorMessage")
    }

    /**
     * Builder para construir una instancia de `ExceptionHandler`.
     *
     */
    class Builder<T : Exception> {
        private var exceptionClass: Class<T>? = null
        private var excFactory: ExceptionFactory<T>? = null

        /**
         * Establece la factoría de excepciones utilizada para crear las instancias de excepciones.
         *
         * @param exceptionFactory La factoría de excepciones a utilizar.
         * @return El builder actualizado.
         */
        fun withExceptionFactory(exceptionFactory: ExceptionFactory<T>) = apply { this.excFactory = exceptionFactory }

        fun withExceptionClass(exceptionClass: Class<T>) = apply { this.exceptionClass = exceptionClass }

        /**
         * Construye una instancia de `ExceptionHandler` utilizando la factoría de excepciones especificada.
         * Si no se proporciona una factoría de excepciones, se utiliza una instancia de `ExceptionFactoryImpl`
         * con la clase de excepción dada.
         *
         * @return La instancia de `ExceptionHandler` creada.
         */
        fun build(): ExceptionHandler<T> = ExceptionHandler(
            excFactory ?: ExceptionFactoryImpl.Builder<T>().apply {
                this@Builder.exceptionClass?.let { withExceptionClass(it) }
            }.build()
        )
    }
}
