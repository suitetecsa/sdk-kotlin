package io.github.suitetecsa.sdk.promotion

import io.github.suitetecsa.sdk.promotion.model.Promotion
import org.jsoup.Jsoup
import java.net.URL

private const val DEFAULT_TIMEOUT = 30000

object PromotionsCollector {
    /**
     * Obtiene las promociones desde la web de ETECSA.
     *
     * @return Objeto `ResultType` que encapsula el resultado de la carga de promociones.
     */
    @JvmStatic
    fun collect() = Jsoup.parse(URL("https://www.etecsa.cu"), DEFAULT_TIMEOUT)
            .select("div.carousel-inner")
            .select("div.carousel-item")
            .map { item ->
                Promotion(
                    svgUrl =
                    "https://www.etecsa.cu${parsePromotionLink(item.selectFirst("div[style]")?.attr("style"))}",
                    jpgUrl = "https://www.etecsa.cu${item.selectFirst("div.mipromocion")?.selectFirst("div.mipromocion-contenido")
                        ?.selectFirst("img")?.attr("src")}",
                    promotionUrl = "https://www.etecsa.cu${item.selectFirst("div.mipromocion")?.selectFirst("div.mipromocion-contenido")
                        ?.selectFirst("a")?.attr("href")}"

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
