package cu.suitetecsa.sdk.nauta.rxjava

import cu.suitetecsa.sdk.nauta.DefaultUserPortalSessionManager
import cu.suitetecsa.sdk.nauta.UserPortalAuthApi
import cu.suitetecsa.sdk.nauta.UserPortalSessionManager
import cu.suitetecsa.sdk.nauta.model.NautaUser
import cu.suitetecsa.sdk.nauta.scraper.AuthUserPortalScraper
import cu.suitetecsa.sdk.nauta.scraper.JsoupAuthUserPortalScrapper
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class UserPortalAuthApi private constructor(
    sessionManager: UserPortalSessionManager,
    scraper: AuthUserPortalScraper
) {
    private val api = UserPortalAuthApi.Builder()
        .withSessionManager(sessionManager)
        .withScraper(scraper)
        .build()

    val captchaImage: Observable<ByteArray> = Observable.fromCallable { api.captchaImage.getOrThrow() }
        .subscribeOn(Schedulers.io())

    val userInformation: Observable<NautaUser> = Observable.fromCallable { api.userInformation.getOrThrow() }
        .subscribeOn(Schedulers.io())

    fun login(username: String, password: String, captchaCode: String): Observable<NautaUser> =
        Observable.fromCallable { api.login(username, password, captchaCode).getOrThrow() }.subscribeOn(Schedulers.io())

    class Builder {
        private var sessionManager: UserPortalSessionManager? = null
        private var scraper: AuthUserPortalScraper? = null

        fun withCommunicator(sessionManager: UserPortalSessionManager) = apply { this.sessionManager = sessionManager }
        fun withScraper(scraper: AuthUserPortalScraper) = apply { this.scraper = scraper }

        fun build(): cu.suitetecsa.sdk.nauta.rxjava.UserPortalAuthApi = UserPortalAuthApi(
            sessionManager ?: DefaultUserPortalSessionManager
                .Builder()
                .build(),
            scraper ?: JsoupAuthUserPortalScrapper
        )
    }
}
