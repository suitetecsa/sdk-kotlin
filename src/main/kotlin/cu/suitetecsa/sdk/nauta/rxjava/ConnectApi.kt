package cu.suitetecsa.sdk.nauta.rxjava

import cu.suitetecsa.sdk.nauta.model.DataSession
import cu.suitetecsa.sdk.nauta.model.NautaConnectInformation
import cu.suitetecsa.sdk.nauta.scraper.ConnectPortalScraper
import cu.suitetecsa.sdk.nauta.scraper.JsoupConnectPortalScraper
import cu.suitetecsa.sdk.network.JsoupPortalCommunicator
import cu.suitetecsa.sdk.network.PortalCommunicator
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import cu.suitetecsa.sdk.nauta.ConnectApi as NautaConnectApi

class ConnectApi private constructor(
    communicator: PortalCommunicator,
    scraper: ConnectPortalScraper
) {
    private val api = NautaConnectApi.Builder()
        .withCommunicator(communicator)
        .withScraper(scraper).build()

    val username: String? = api.username
    val password: String? = api.password

    var dataSession: DataSession?
        get() = api.dataSession
        set(value) {
            api.dataSession = value
        }

    val remainingTime: Observable<Long> = Observable.fromCallable { api.remainingTime.getOrThrow() }
        .subscribeOn(Schedulers.io())

    val connectInformation: Observable<NautaConnectInformation> =
        Observable.fromCallable { api.connectInformation.getOrThrow() }.subscribeOn(Schedulers.io())

    fun setCredentials(username: String, password: String) = api.setCredentials(username, password)

    fun connect(): Observable<String> = Observable.fromCallable { api.connect().getOrThrow() }
        .subscribeOn(Schedulers.io())

    fun disconnect(): Observable<String> = Observable.fromCallable { api.disconnect().getOrThrow() }
        .subscribeOn(Schedulers.io())

    class Builder {
        private var communicator: PortalCommunicator? = null
        private var scraper: ConnectPortalScraper? = null

        fun withCommunicator(communicator: PortalCommunicator): Builder {
            this.communicator = communicator
            return this
        }

        fun withScraper(scraper: ConnectPortalScraper): Builder {
            this.scraper = scraper
            return this
        }

        fun build(): ConnectApi {
            return ConnectApi(
                communicator ?: JsoupPortalCommunicator.Builder().build(),
                scraper ?: JsoupConnectPortalScraper.Builder().build()
            )
        }
    }
}
