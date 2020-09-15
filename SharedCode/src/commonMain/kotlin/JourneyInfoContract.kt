package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface JourneyInfoContract {
    interface View {
        fun setJourney(journey: JourneyInfo)
        fun setTickets(tickets: List<TicketInfo>)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: JourneyInfoContract.View)
    }
}