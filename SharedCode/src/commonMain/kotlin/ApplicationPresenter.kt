package com.jetbrains.handson.mpp.mobile

import io.ktor.http.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var view: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()
    private val stations: List<Station> = listOf(
        Station("Kings Cross", "KGX"),
        Station("York", "YRK"),
        Station("Edinburgh Waverley", "EDB"),
        Station("Leeds", "LDS"),
        Station("Cattal", "CTL")
    )

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setLabel(createApplicationScreenMessage())
        view.setStations(stations);
    }

    override fun onTimesRequested() {
        val departureStation = view?.getDepartureStation();
        val arrivalStation = view?.getArrivalStation();

        val now = "2020-10-14T19:30:00.000+01:00";

        val builder = URLBuilder(
            URLProtocol.HTTPS,
            "mobile-api-dev.lner.co.uk",
            DEFAULT_PORT
        )

        builder.path("v1", "fares");
        builder.parameters.append("originStation", departureStation!!.id);
        builder.parameters.append("destinationStation", arrivalStation!!.id);
        builder.parameters.append("noChanges", "false");
        builder.parameters.append("numberOfAdults", "2");
        builder.parameters.append("numberOfChildren", "0")
        builder.parameters.append("journeyType", "single")
        builder.parameters.append("inboundDateTime", now)
        builder.parameters.append("inboundIsArriveBy", "false");
        builder.parameters.append("outboundDateTime", now)
        builder.parameters.append("outboundIsArriveBy", "false")

        view?.openUrl(builder.buildString());
    }
}
