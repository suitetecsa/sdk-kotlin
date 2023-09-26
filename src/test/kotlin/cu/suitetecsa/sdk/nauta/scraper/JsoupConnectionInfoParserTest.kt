package cu.suitetecsa.sdk.nauta.scraper

import cu.suitetecsa.sdk.nauta.model.AccountInfo
import cu.suitetecsa.sdk.nauta.model.LastConnection
import cu.suitetecsa.sdk.nauta.model.NautaConnectInformation
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JsoupConnectionInfoParserTest {

    private lateinit var parser: ConnectionInfoParser

    @BeforeEach
    fun setUp() {
        parser = JsoupConnectionInfoParser
    }

    @Test
    fun testParseCheckConnectionsWhenHtmlContainsConnectDomainThenReturnFalse() {
        // Arrange
        val html = "<html><body>Some content with secure.etecsa.net</body></html>"

        // Act
        val result = parser.parseCheckConnections(html)

        // Assert
        assertFalse(result, "Expected false when HTML contains connectDomain")
    }

    @Test
    fun testParseCheckConnectionsWhenHtmlDoesNotContainConnectDomainThenReturnTrue() {
        // Arrange
        val html = "<html><body>Some content without connectDomain</body></html>"

        // Act
        val result = parser.parseCheckConnections(html)

        // Assert
        assertTrue(result, "Expected true when HTML does not contain connectDomain")
    }

    @Test
    fun testParseNautaConnectInformationWhenHtmlIsValidThenReturnsCorrectInfo() {
        // Arrange
        val html = "<div id=\"userinfo\"><table id=\"sessioninfo\"><tbody><tr><td class=\"key\">Estado de la cuenta:" +
            "</td><td>Activa</td></tr><tr><td class=\"key\">Crédito:</td><td>37.82 CUP</td></tr><tr>" +
            "<td class=\"key\">Fecha de expiración:</td><td>No especificada</td></tr><tr><td class=\"key\">" +
            "Áreas de acceso:</td><td>Acceso desde todas las áreas de Internet</td></tr></tbody></table><br>" +
            "<table id=\"sesiontraza\"><caption>Últimas sesiones</caption><thead><tr><th>Desde</th><th>Hasta</th>" +
            "<th>iempo</th></tr></thead><tbody><tr><td>2022/05/21 07:07:17</td><td>2022/05/21 07:15:42</td>" +
            "<td>00:08:25</td></tr></tbody></table></div>"

        // Act
        val result = parser.parseNautaConnectInformation(html)

        // Assert
        assertEquals(
            expected = NautaConnectInformation(
                AccountInfo("Acceso desde todas las áreas de Internet", "Activa", "37.82 CUP", "No especificada"),
                listOf(LastConnection("2022/05/21 07:07:17", "00:08:25", "2022/05/21 07:15:42"))
            ),
            actual = result
        )
    }

    @Test
    fun testParseNautaConnectInformationWhenHtmlIsEmptyThenReturnsDefaultInfo() {
        // Arrange
        val html = "<html>...</html>" // replace with invalid HTML

        // Act
        val result = parser.parseNautaConnectInformation(html)

        // Act & Assert
        assertEquals(
            expected = NautaConnectInformation(
                AccountInfo("", "", "", ""),
                listOf()
            ),
            actual = result
        )
    }
}
