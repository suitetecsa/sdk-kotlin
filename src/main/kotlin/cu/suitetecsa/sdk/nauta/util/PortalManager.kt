package cu.suitetecsa.sdk.nauta.util

sealed class PortalManager(val baseUrl: String) {
    data object Connect : PortalManager("https://$connectDomain:8443")
    data object User : PortalManager("https://www.portal.nauta.cu")
}
