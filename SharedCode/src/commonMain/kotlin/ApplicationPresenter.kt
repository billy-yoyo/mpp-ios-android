package com.jetbrains.handson.mpp.mobile

import com.jetbrains.handson.mpp.mobile.dataclasses.Journey
import com.jetbrains.handson.mpp.mobile.dataclasses.Station
import com.jetbrains.handson.mpp.mobile.dataclasses.Ticket
import com.jetbrains.handson.mpp.mobile.models.FaresModel
import com.jetbrains.handson.mpp.mobile.repository.FaresRepository
import com.jetbrains.handson.mpp.mobile.repository.JourneyRepository
import com.jetbrains.handson.mpp.mobile.repository.StationRepository
import com.jetbrains.handson.mpp.mobile.models.StationListModel
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

    private val stationRepository by StationRepository.lazyGet()
    private val journeyRepository by JourneyRepository.lazyGet()
    private val faresRepository by FaresRepository.lazyGet()

    private val dispatchers = AppDispatchersImpl()
    private lateinit var view: ApplicationContract.View
    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        listOfStations()
    }

    private fun listOfStations() {
        launch {
            val builder = URLBuilder(
                URLProtocol.HTTPS,
                "mobile-api-dev.lner.co.uk",
                DEFAULT_PORT
            )
            builder.path("v1", "stations")
            val stationsModel: StationListModel = client.get(builder.buildString())
            val stationsList: List<Station> =
                stationsModel.stations.map { Station(it.name, it.crs, it.nlc) }.sortedBy { station -> station.name.toLowerCase() }
            view.setStations(stationsList)
        }
    }

    override fun onTimesRequested() {
        launch {
            view.setJourneys(listOf()) // clear journeys

            val model = getTrainTimeData(
                stationRepository.getDepartureStation()!!,
                stationRepository.getArrivalStation()!!
            )

            faresRepository.setFares(model)

            val journeys: MutableList<Journey> = mutableListOf()

            model.outboundJourneys.forEachIndexed { id, journey ->
                if (journey.tickets.isNotEmpty()) {
                    val minPrice = journey.tickets.minBy { it.priceInPennies }!!.priceInPennies
                    val maxPrice = journey.tickets.maxBy { it.priceInPennies }!!.priceInPennies
                    journeys.add(
                        Journey(
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

    override fun onViewJourney(journey: Journey) {
        val journeyModel = faresRepository.getFares()!!.outboundJourneys[journey.id]

        val tickets = journeyModel.tickets.map { ticket ->
            Ticket(
                ticket.name,
                ticket.description,
                ticket.priceInPennies
            )
        }.sortedBy { ticket -> ticket.price }

        journeyRepository.setJourney(journey)
        journeyRepository.setTickets(tickets)

        view.openJourneyView()
    }

    private suspend fun getTrainTimeData(departureStation: Station, arrivalStation: Station): FaresModel {
        val now = DateTime.nowLocal().plus(DateTimeSpan(minutes=1)).format("YYYY-MM-dd'T'HH:mm:ss.SSSXXX")

        val builder = URLBuilder(
            URLProtocol.HTTPS,
            "mobile-api-dev.lner.co.uk",
            DEFAULT_PORT
        )

        builder.path("v1", "fares")
        builder.parameters.append("originStation", departureStation.apiCode)
        builder.parameters.append("destinationStation", arrivalStation.apiCode)
        builder.parameters.append("noChanges", "false")
        builder.parameters.append("numberOfAdults", "2")
        builder.parameters.append("numberOfChildren", "0")
        builder.parameters.append("journeyType", "single")
        builder.parameters.append("outboundDateTime", now)
        builder.parameters.append("outboundIsArriveBy", "false")

        return client.get(builder.buildString())
    }

    override fun setDepartureStation(station: Station?) {
        stationRepository.setDepartureStation(station)
    }

    override fun setArrivalStation(station: Station?) {
        stationRepository.setArrivalStation(station)
    }
}