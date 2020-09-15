package com.jetbrains.handson.mpp.mobile

import com.jetbrains.handson.mpp.mobile.dataclasses.Journey
import com.jetbrains.handson.mpp.mobile.dataclasses.Ticket
import kotlinx.coroutines.CoroutineScope

interface JourneyInfoContract {
    interface View {
        fun setJourney(journey: Journey)
        fun setTickets(tickets: List<Ticket>)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: JourneyInfoContract.View)
    }
}