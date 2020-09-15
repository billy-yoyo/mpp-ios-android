package com.jetbrains.handson.mpp.mobile.repository

import com.jetbrains.handson.mpp.mobile.dataclasses.Journey
import com.jetbrains.handson.mpp.mobile.dataclasses.Ticket
import kotlin.native.concurrent.ThreadLocal

interface JourneyRepository {
    fun getJourney(): Journey?
    fun setJourney(journey: Journey?)

    fun getTickets(): List<Ticket>
    fun setTickets(tickets: List<Ticket>)

    @ThreadLocal
    companion object : Provider<JourneyRepository>() {
        override fun create(): JourneyRepository = RepositoryProvider.getJourneyRepository()
    }
}