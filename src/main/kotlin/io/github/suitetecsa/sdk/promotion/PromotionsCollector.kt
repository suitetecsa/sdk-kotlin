package io.github.suitetecsa.sdk.promotion

import io.github.suitetecsa.sdk.promotion.model.Promotion
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.URL

private const val DEFAULT_TIMEOUT = 30000
private const val BASE_URL = "https://www.etecsa.cu"

object PromotionsCollector {
    /**
     * Obtiene las promociones desde la web de ETECSA.
     *
     * @return Objeto `ResultType` que encapsula el resultado de la carga de promociones.
     */
    @JvmStatic
    @JvmOverloads
    fun collect(mobileAspect: Boolean = false) = Jsoup.parse(URL("https://www.etecsa.cu"), DEFAULT_TIMEOUT)
        .let {
            if (mobileAspect) {
                it.select("div#carousel-dm>div").map(::parsePromotionMobile)
            } else {
                it.select("div.carousel-inner")
                    .select("div.carousel-item")
                    .map { item -> parsePromotion(item) }
            }
        }

    private fun parsePromotion(item: Element): Promotion {
        val background = parsePromotionLink(item.selectFirst("div[style]")?.attr("style"))
        val promotionContent = item.selectFirst("div.mipromocion")?.selectFirst("div.mipromocion-contenido")
        val svg = promotionContent?.selectFirst("img")?.attr("src")
        val url = promotionContent?.selectFirst("a")?.attr("href")
        return Promotion("$BASE_URL$svg", "$BASE_URL$background", "$BASE_URL$url")
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

    private fun parsePromotionMobile(element: Element): Promotion {
        val background = element.selectFirst("img")!!.attr("src")
        val svg = element.selectFirst("div.mipromocion-contenido-movil")!!
            .selectFirst("img")!!.attr("src")
        val url = element.selectFirst("div.mipromocion-contenido-movil-botones")!!
            .selectFirst("a")!!.attr("href")
        return Promotion("$BASE_URL$svg", "$BASE_URL$background", "$BASE_URL$url")
    }
}
