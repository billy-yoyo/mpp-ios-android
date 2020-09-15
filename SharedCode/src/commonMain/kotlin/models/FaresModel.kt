package com.jetbrains.handson.mpp.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class FaresModel(
    val outboundJourneys: List<OutboundJourneyModel>
)