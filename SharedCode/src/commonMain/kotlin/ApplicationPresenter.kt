package com.jetbrains.handson.mpp.mobile

import io.ktor.http.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var _view: ApplicationContract.View? = null
    private val view: ApplicationContract.View get() = _view!!
    private val job: Job = SupervisorJob()
    private val stations: List<Station> = listOf(
        Station("Kings Cross", "KGX"),
        Station("York", "YRK"),
        Station("Edinburgh Waverley", "EDB"),
        Station("Leeds", "LDS"),
        Station("Cattal", "CTL")
    )

    private var departureStation: Station? = null
    private var arrivalStation: Station? = null

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this._view = view
        view.setLabel(createApplicationScreenMessage())
        view.setStations(stations);
    }

    override fun onTimesRequested() {
        val now = "2020-10-14T19:30:00.000+01:00"

        val builder = URLBuilder(
            URLProtocol.HTTPS,
            "mobile-api-dev.lner.co.uk",
            DEFAULT_PORT
        )

        builder.path("v1", "fares")
        builder.parameters.append("originStation", departureStation!!.id)
        builder.parameters.append("destinationStation", arrivalStation!!.id)
        builder.parameters.append("noChanges", "false")
        builder.parameters.append("numberOfAdults", "2")
        builder.parameters.append("numberOfChildren", "0")
        builder.parameters.append("journeyType", "single")
        builder.parameters.append("outboundDateTime", now)
        builder.parameters.append("outboundIsArriveBy", "false")

        view.openUrl(builder.buildString());
    }

    override fun setDepartureStation(station: Station?) {
        departureStation = station
    }

    override fun setArrivalStation(station: Station?) {
        arrivalStation = station
    }
}
