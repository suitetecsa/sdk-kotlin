package cu.suitetecsa.sdk.nauta.rxjava

import cu.suitetecsa.sdk.nauta.model.DataSession
import cu.suitetecsa.sdk.nauta.model.NautaConnectInformation
import cu.suitetecsa.sdk.nauta.scraper.ConnectPortalScraper
import cu.suitetecsa.sdk.network.PortalCommunicator
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import cu.suitetecsa.sdk.nauta.ConnectApi as NautaConnectApi

object ConnectApi {

    fun withPortalCommunicator(portalCommunicator: PortalCommunicator) =
        NautaConnectApi.withPortalCommunicator(portalCommunicator)

    fun withPortalScraper(portalScraper: ConnectPortalScraper) =
        NautaConnectApi.withPortalScraper(portalScraper)

    fun getRemainingTime(dataSession: DataSession): Observable<Long> = Observable.fromCallable {
        NautaConnectApi.getRemainingTime(dataSession).getOrThrow()
    }.subscribeOn(Schedulers.io())

    fun getUserInformation(username: String, password: String): Observable<NautaConnectInformation> =
        Observable.fromCallable {
            NautaConnectApi.getUserInformation(username, password).getOrThrow()
        }.subscribeOn(Schedulers.io())

    fun connect(username: String, password: String): Observable<DataSession> = Observable.fromCallable {
        NautaConnectApi.connect(username, password).getOrThrow()
    }.subscribeOn(Schedulers.io())

    fun disconnect(dataSession: DataSession): Observable<Unit> =
        Observable.fromCallable { NautaConnectApi.disconnect(dataSession).getOrThrow() }
            .subscribeOn(Schedulers.io())
}
