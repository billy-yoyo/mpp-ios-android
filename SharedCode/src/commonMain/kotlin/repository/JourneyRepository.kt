package com.jetbrains.handson.mpp.mobile.repository

import com.jetbrains.handson.mpp.mobile.JourneyInfo
import com.jetbrains.handson.mpp.mobile.TicketInfo
import kotlin.native.concurrent.ThreadLocal

interface JourneyRepository {
    fun getJourney(): JourneyInfo?
    fun setJourney(journey: JourneyInfo?)

    fun getTickets(): List<TicketInfo>
    fun setTickets(tickets: List<TicketInfo>)

    @ThreadLocal
    companion object : Provider<JourneyRepository>() {
        override fun create(): JourneyRepository = RepositoryProvider.getJourneyRepository()
    }
}