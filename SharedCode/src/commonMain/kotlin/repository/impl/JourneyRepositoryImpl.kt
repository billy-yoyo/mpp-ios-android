package com.jetbrains.handson.mpp.mobile.repository.impl

import com.jetbrains.handson.mpp.mobile.JourneyInfo
import com.jetbrains.handson.mpp.mobile.TicketInfo
import com.jetbrains.handson.mpp.mobile.repository.JourneyRepository

class JourneyRepositoryImpl : JourneyRepository {
    private var journey: JourneyInfo? = null
    private var tickets: List<TicketInfo> = listOf()

    override fun getJourney(): JourneyInfo? {
        return journey
    }

    override fun setJourney(journey: JourneyInfo?) {
        this.journey = journey
    }

    override fun getTickets(): List<TicketInfo> {
        return tickets
    }

    override fun setTickets(tickets: List<TicketInfo>) {
        this.tickets = tickets
    }
}