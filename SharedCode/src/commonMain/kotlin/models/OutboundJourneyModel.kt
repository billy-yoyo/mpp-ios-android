package com.jetbrains.handson.mpp.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class OutboundJourneyModel(
    val originStation: StationModel,
    val destinationStation: StationModel,
    val departureTime: String,
    val arrivalTime: String,
    val tickets: List<TicketModel>
)