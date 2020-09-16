package com.jetbrains.handson.mpp.mobile

import com.jetbrains.handson.mpp.mobile.dataclasses.Journey
import com.jetbrains.handson.mpp.mobile.dataclasses.Station
import com.jetbrains.handson.mpp.mobile.dataclasses.Ticket
import com.jetbrains.handson.mpp.mobile.http.TrainBoardAPI
import com.jetbrains.handson.mpp.mobile.models.FaresModel
import com.jetbrains.handson.mpp.mobile.repository.FaresRepository
import com.jetbrains.handson.mpp.mobile.repository.JourneyRepository
import com.jetbrains.handson.mpp.mobile.repository.StationRepository
import com.jetbrains.handson.mpp.mobile.models.StationListModel
import com.soywiz.klock.DateTime
import com.soywiz.klock.DateTimeSpan
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter: ApplicationContract.Presenter() {

    private val stationRepository by StationRepository.lazyGet()
    private val journeyRepository by JourneyRepository.lazyGet()
    private val faresRepository by FaresRepository.lazyGet()

    private val dispatchers = AppDispatchersImpl()
    private lateinit var view: ApplicationContract.View
    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    private val api = TrainBoardAPI {
        view.showAlert(it.message)
    }

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        listOfStations()
    }

    private fun listOfStations() {
        launch {
            val stationsModel: StationListModel = api.getStationListModel()
            val stationsList: List<Station> =
                stationsModel.stations.map { Station(it.name, it.crs, it.nlc) }
                    .sortedBy { station -> station.name.toLowerCase() }

            view.setStations(stationsList)
        }
    }

    override fun onTimesRequested() {
        launch {
            val model = api.getFaresModel(
                departureStation = stationRepository.getDepartureStation()!!,
                arrivalStation =  stationRepository.getArrivalStation()!!,
                outboundDateTime = DateTime.nowLocal().plus(DateTimeSpan(minutes=1))
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

    override fun setDepartureStation(station: Station?) {
        stationRepository.setDepartureStation(station)
    }

    override fun setArrivalStation(station: Station?) {
        stationRepository.setArrivalStation(station)
    }
}