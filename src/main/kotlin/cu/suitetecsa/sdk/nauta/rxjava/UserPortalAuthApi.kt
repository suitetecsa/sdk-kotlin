package cu.suitetecsa.sdk.nauta.rxjava

import cu.suitetecsa.sdk.nauta.UserPortalAuthApi
import cu.suitetecsa.sdk.nauta.model.NautaUser
import cu.suitetecsa.sdk.nauta.network.UserPortalSession
import cu.suitetecsa.sdk.nauta.scraper.AuthUserPortalScraper
import cu.suitetecsa.sdk.nauta.scraper.JsoupAuthUserPortalScrapper
import cu.suitetecsa.sdk.network.JsoupPortalCommunicator
import cu.suitetecsa.sdk.network.PortalCommunicator
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class UserPortalAuthApi private constructor(
    communicator: PortalCommunicator,
    scraper: AuthUserPortalScraper
) {
    private val api = UserPortalAuthApi.Builder()
        .withCommunicator(communicator)
        .withScraper(scraper)
        .build()

    val isNautaHome: Boolean
        get() = api.isNautaHome

    fun setCredentials(username: String, password: String) = api.setCredentials(username, password)

    val captchaImage: Observable<ByteArray> = Observable.fromCallable { api.captchaImage.getOrThrow() }
        .subscribeOn(Schedulers.io())

    val userInformation: Observable<NautaUser> = Observable.fromCallable { api.userInformation.getOrThrow() }
        .subscribeOn(Schedulers.io())

    fun login(captchaCode: String): Observable<NautaUser> =
        Observable.fromCallable { api.login(captchaCode).getOrThrow() }.subscribeOn(Schedulers.io())

    class Builder {
        private var communicator: PortalCommunicator? = null
        private var scraper: AuthUserPortalScraper? = null

        fun withCommunicator(communicator: PortalCommunicator) = apply { this.communicator = communicator }
        fun withScraper(scraper: AuthUserPortalScraper) = apply { this.scraper = scraper }

        fun build(): cu.suitetecsa.sdk.nauta.rxjava.UserPortalAuthApi = UserPortalAuthApi(
            communicator ?: JsoupPortalCommunicator
                .Builder()
                .withSession(UserPortalSession)
                .build(),
            scraper ?: JsoupAuthUserPortalScrapper
        )
    }
}
