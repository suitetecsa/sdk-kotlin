package cu.suitetecsa.sdk.nauta.scraper

import cu.suitetecsa.sdk.nauta.model.ConnectionsSummary
import cu.suitetecsa.sdk.nauta.model.QuotesPaidSummary
import cu.suitetecsa.sdk.nauta.model.RechargesSummary
import cu.suitetecsa.sdk.nauta.model.TransfersSummary

interface ActionsSummaryParser {
    /**
     * Analiza el HTML para extraer un resumen de conexiones.
     *
     * @param html El contenido HTML a analizar.
     * @return El objeto `ConnectionsSummary` que contiene el resumen de conexiones.
     */
    fun parseConnectionsSummary(html: String): ConnectionsSummary

    /**
     * Analiza el HTML para extraer un resumen de recargas.
     *
     * @param html El contenido HTML a analizar.
     * @return El objeto `RechargesSummary` que contiene el resumen de recargas.
     */
    fun parseRechargesSummary(html: String): RechargesSummary

    /**
     * Analiza el HTML para extraer un resumen de transferencias.
     *
     * @param html El contenido HTML a analizar.
     * @return El objeto `TransfersSummary` que contiene el resumen de transferencias.
     */
    fun parseTransfersSummary(html: String): TransfersSummary

    /**
     * Analiza el HTML para extraer un resumen de cotizaciones pagadas.
     *
     * @param html El contenido HTML a analizar.
     * @return El objeto `QuotesPaidSummary` que contiene el resumen de cotizaciones pagadas.
     */
    fun parseQuotesPaidSummary(html: String): QuotesPaidSummary
}
