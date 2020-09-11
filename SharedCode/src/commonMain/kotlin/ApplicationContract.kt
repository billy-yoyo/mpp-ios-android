package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setLabel(text: String)

        fun setStations(stations: List<Station>)

        fun showAlert(message: String)

        fun openUrl(url: String)

        fun setTickets(tickets: List<TicketInfo>)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)

        abstract fun onTimesRequested()

        abstract fun setDepartureStation(station: Station?)

        abstract fun setArrivalStation(station: Station?)
    }
}
