package cu.suitetecsa.sdk.nauta.scraper

import cu.suitetecsa.sdk.nauta.model.Connection
import cu.suitetecsa.sdk.nauta.model.QuotePaid
import cu.suitetecsa.sdk.nauta.model.Recharge
import cu.suitetecsa.sdk.nauta.model.Transfer

interface ActionsParser {
    /**
     * Analiza el HTML para extraer la lista de conexiones.
     *
     * @param html El contenido HTML a analizar.
     * @return La lista de objetos `Connection` que contienen la información de las conexiones.
     */
    fun parseConnections(html: String): List<Connection>

    /**
     * Analiza el HTML para extraer la lista de recargas.
     *
     * @param html El contenido HTML a analizar.
     * @return La lista de objetos `Recharge` que contienen la información de las recargas.
     */
    fun parseRecharges(html: String): List<Recharge>

    /**
     * Analiza el HTML para extraer la lista de transferencias.
     *
     * @param html El contenido HTML a analizar.
     * @return La lista de objetos `Transfer` que contienen la información de las transferencias.
     */
    fun parseTransfers(html: String): List<Transfer>

    /**
     * Analiza el HTML para extraer la lista de cotizaciones pagadas.
     *
     * @param html El contenido HTML a analizar.
     * @return La lista de objetos `QuotePaid` que contienen la información de las cotizaciones pagadas.
     */
    fun parseQuotesPaid(html: String): List<QuotePaid>
}
