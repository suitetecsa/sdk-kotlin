package io.github.suitetecsa.sdk.promotion

import io.github.suitetecsa.sdk.network.HttpResponse
import io.github.suitetecsa.sdk.network.PortalCommunicator
import io.github.suitetecsa.sdk.promotion.model.Promotion
import org.jsoup.Jsoup

object PromotionsCollector {
    /**
     * Obtiene las promociones desde la web de ETECSA.
     *
     * @return Objeto `ResultType` que encapsula el resultado de la carga de promociones.
     */
    @JvmStatic
    fun collect() = PortalCommunicator
        .Builder()
        .build()
        .performRequest("https://www.etecsa.cu", ::parse)

    /**
     * Analiza las promociones a partir del HTML proporcionado y devuelve una lista de objetos `Promotion`.
     *
     * @param response La respuesta que contiene las promociones a analizar.
     * @return Una lista de objetos `Promotion` que representan las promociones analizadas.
     */
    private fun parse(response: HttpResponse) = Jsoup.parse(response.text)
            .select("div.carousel-inner")
            .select("div.carousel-item")
            .map { item ->
                Promotion(
                    svgUrl = parsePromotionLink(item.selectFirst("div[style]")?.attr("style")) ?: "",
                    jpgUrl = item.selectFirst("div.mipromocion")?.selectFirst("div.mipromocion-contenido")
                        ?.selectFirst("img")?.attr("src") ?: "",
                    promotionUrl = item.selectFirst("div.mipromocion")?.selectFirst("div.mipromocion-contenido")
                        ?.selectFirst("a")?.attr("href") ?: ""

                )
            }

    /**
     * Analiza el enlace de la promoción a partir del texto proporcionado.
     *
     * @param text El texto que contiene el enlace de la promoción.
     * @return El enlace de la promoción analizado.
     */
    private fun parsePromotionLink(text: String?): String? {
        val regex = "url\\('(.+)'\\);".toRegex()
        val matchResult = text?.let { regex.find(it) }
        return matchResult?.groupValues?.get(1)
    }
}
