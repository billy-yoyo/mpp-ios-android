package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setStations(stations: List<Station>)

        fun showAlert(message: String)

        fun openUrl(url: String)

        fun setJourneys(journeys: List<JourneyInfo>)

        fun openJourneyView(journey: JourneyInfo, tickets: List<TicketInfo>)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)

        abstract fun onTimesRequested()

        abstract fun onViewJourney(journey: JourneyInfo)

        abstract fun setDepartureStation(station: Station?)

        abstract fun setArrivalStation(station: Station?)
    }
}
