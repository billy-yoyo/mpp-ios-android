package com.jetbrains.handson.mpp.mobile.repository.impl

import com.jetbrains.handson.mpp.mobile.dataclasses.Journey
import com.jetbrains.handson.mpp.mobile.dataclasses.Ticket
import com.jetbrains.handson.mpp.mobile.repository.JourneyRepository

class JourneyRepositoryImpl : JourneyRepository {
    private var journey: Journey? = null
    private var tickets: List<Ticket> = listOf()

    override fun getJourney(): Journey? {
        return journey
    }

    override fun setJourney(journey: Journey?) {
        this.journey = journey
    }

    override fun getTickets(): List<Ticket> {
        return tickets
    }

    override fun setTickets(tickets: List<Ticket>) {
        this.tickets = tickets
    }
}