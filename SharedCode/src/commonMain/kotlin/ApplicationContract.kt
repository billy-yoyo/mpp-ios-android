package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setLabel(text: String)

        fun setStations(stations: List<Station>)

        fun showAlert(message: String)

        fun openUrl(url: String)

        fun getDepartureStation(): Station

        fun getArrivalStation(): Station

        fun addTicket(ticket: TicketInfo)

        fun setContext(context: ViewContext)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)

        abstract fun onTimesRequested()
    }
}
