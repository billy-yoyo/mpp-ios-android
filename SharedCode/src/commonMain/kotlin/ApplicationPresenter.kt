package com.jetbrains.handson.mpp.mobile

import com.jetbrains.handson.mpp.mobile.models.FaresModel
import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeSpan
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter: ApplicationContract.Presenter() {

    private val client = HttpClient() {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {ignoreUnknownKeys=true})
        }
    }

    private val dispatchers = AppDispatchersImpl()
    private lateinit var view: ApplicationContract.View
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
    private var model: FaresModel? = null

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setLabel(createApplicationScreenMessage())
        view.setStations(stations)
    }

    override fun onTimesRequested() {
        val view = this.view

        launch {
            view.setJourneys(listOf()) // clear journeys

            model = getTrainTimeData(departureStation!!, arrivalStation!!)
            val journeys: MutableList<JourneyInfo> = mutableListOf()

            model!!.outboundJourneys.forEachIndexed { id, journey ->
                if (journey.tickets.isNotEmpty()) {
                    val minPrice = journey.tickets.minBy { it.priceInPennies }!!.priceInPennies
                    val maxPrice = journey.tickets.maxBy { it.priceInPennies }!!.priceInPennies
                    journeys.add(
                        JourneyInfo(
                            id,
                            journey.departureTime,
                            journey.arrivalTime,
                            minPrice,
                            maxPrice
                        )
                    )
                }
            }
            view.setJourneys(journeys)
        }
    }

    override fun onViewJourney(journey: JourneyInfo) {
        val journeyModel = model!!.outboundJourneys[journey.id]

        val tickets = journeyModel.tickets.map { ticket ->
            TicketInfo(
                ticket.name,
                ticket.description,
                ticket.priceInPennies
            )
        }

        view.openJourneyView(journey, tickets)
    }

    private suspend fun getTrainTimeData(departureStation: Station, arrivalStation: Station): FaresModel {
        val now = DateTime.nowLocal().plus(DateTimeSpan(minutes=1)).format("YYYY-MM-dd'T'HH:mm:ss.SSSXXX")

        val builder = URLBuilder(
            URLProtocol.HTTPS,
            "mobile-api-dev.lner.co.uk",
            DEFAULT_PORT
        )

        builder.path("v1", "fares")
        builder.parameters.append("originStation", departureStation.id)
        builder.parameters.append("destinationStation", arrivalStation.id)
        builder.parameters.append("noChanges", "false")
        builder.parameters.append("numberOfAdults", "2")
        builder.parameters.append("numberOfChildren", "0")
        builder.parameters.append("journeyType", "single")
        builder.parameters.append("outboundDateTime", now)
        builder.parameters.append("outboundIsArriveBy", "false")

        return client.get(builder.buildString())
    }

    override fun setDepartureStation(station: Station?) {
        departureStation = station
    }

    override fun setArrivalStation(station: Station?) {
        arrivalStation = station
    }
}