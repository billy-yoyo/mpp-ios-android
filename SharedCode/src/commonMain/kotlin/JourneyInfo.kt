package com.jetbrains.handson.mpp.mobile

import kotlinx.serialization.Serializable

@Serializable
data class JourneyInfo(
    val id: Int,
    val departureTime: String,
    val arrivalTime: String,
    val minPrice: Int,
    val maxPrice: Int
)