package cu.suitetecsa.sdk.nauta.scraper

import cu.suitetecsa.sdk.nauta.exception.LoadInfoException
import org.junit.jupiter.api.assertThrows

class JsoupActionsParserTest {

    // Tests that the parseConnections method successfully parses a list of connections from the HTML string
    fun test_parse_connections_successfully() {
        val html = "<table cellpadding=\"3\" cellspacing=\"0\" class=\"striped bordered highlight responsive-table\"" +
            " style=\"width:100%\"><thead><tr><th>Inicio de sesión</th><th>Fin de sesión</th>" +
            "<th>Tiempo consumido</th><th>Subida</th><th>Descarga</th><th>Importe</th></tr></thead><tbody><tr>" +
            "<td>18/03/2023 12:58:10</td><td>18/03/2023 13:04:28</td><td>00:06:18</td><td>918,00 KB</td>" +
            "<td>16,63 MB</td><td>$1,26</td></tr><tr><td>18/03/2023 12:24:48</td><td>18/03/2023 12:51:59</td>" +
            "<td>00:27:11</td><td>5,43 MB</td><td>100,50 MB</td><td>$5,44</td></tr></tbody></table>"
        JsoupActionsParser.parseConnections(html)
        // Assert statements
    }

    // Tests that the parseRecharges method successfully parses a list of recharges from the HTML string
    fun test_parse_recharges_successfully() {
        val html = "<table cellpadding=\"3\" cellspacing=\"0\" class=\"striped bordered highlight responsive-table\"" +
            " style=\"width:100%\"><thead><tr><th>Fecha</th><th>Importe</th><th>Canal</th><th>Tipo</th></tr>" +
            "</thead><tbody><tr><td>10/03/2023 12:12:33</td><td>\$200,00</td><td>PV ETECSA</td><td>Efectivo</td>" +
            "</tr><tr><td>16/03/2023 19:41:35</td><td>\$250,00</td><td>Transfermóvil</td><td>Efectivo</td></tr>" +
            "</tbody></table>"
        JsoupActionsParser.parseRecharges(html)
        // Assert statements
    }

    // Tests that the parseTransfers method successfully parses a list of transfers from the HTML string
    fun test_parse_transfers_successfully() {
        val html = "<html>...</html>"
        JsoupActionsParser.parseTransfers(html)
        // Assert statements
    }

    // Tests that the parseQuotesPaid method successfully parses a list of quotes paid from the HTML string
    fun test_parse_quotes_paid_successfully() {
        val html = "<table cellpadding=\"3\" cellspacing=\"0\" class=\"striped bordered highlight responsive-table\"" +
            " style=\"width:100%\"><thead><tr><th>Fecha</th><th>Importe</th><th>Canal</th><th>Oficina</th>" +
            "<th>Tipo</th></tr></thead><tbody><tr><td>10/03/2023 12:11:36</td><td>\$300,00</td>" +
            "<td>Oficina Comercial</td><td>PV Sala de Navegación MTC</td><td>Pago de Cuota</td></tr></tbody></table>"
        JsoupActionsParser.parseQuotesPaid(html)
        // Assert statements
    }

    // Tests that the parse methods handle an empty HTML string correctly and return an empty list
    fun test_parse_empty_html_string() {
        val html = ""
        JsoupActionsParser.parseConnections(html)
        JsoupActionsParser.parseRecharges(html)
        JsoupActionsParser.parseTransfers(html)
        JsoupActionsParser.parseQuotesPaid(html)
        // Assert statements for each list
    }

    // Tests that the parse methods handle an invalid HTML string correctly and throw an exception
    fun test_parse_invalid_html_string() {
        val html = "<html>...</html>"
        // Modify the HTML string to make it invalid
        val invalidHtml = html.replace("<tr>", "<div>")
        assertThrows<LoadInfoException> {
            JsoupActionsParser.parseConnections(invalidHtml)
        }
        assertThrows<LoadInfoException> {
            JsoupActionsParser.parseRecharges(invalidHtml)
        }
        assertThrows<LoadInfoException> {
            JsoupActionsParser.parseTransfers(invalidHtml)
        }
        assertThrows<LoadInfoException> {
            JsoupActionsParser.parseQuotesPaid(invalidHtml)
        }
    }
}
