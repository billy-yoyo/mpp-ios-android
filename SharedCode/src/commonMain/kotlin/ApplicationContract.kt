package com.jetbrains.handson.mpp.mobile

import com.jetbrains.handson.mpp.mobile.dataclasses.Journey
import com.jetbrains.handson.mpp.mobile.dataclasses.Station
import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setStations(stations: List<Station>)

        fun showAlert(message: String)

        fun openUrl(url: String)

        fun setJourneys(journeys: List<Journey>)

        fun openJourneyView()
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)

        abstract fun onTimesRequested()

        abstract fun onViewJourney(journey: Journey)

        abstract fun setDepartureStation(station: Station?)

        abstract fun setArrivalStation(station: Station?)
    }
}
