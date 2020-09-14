package com.jetbrains.handson.mpp.mobile

import java.io.Serializable

class JourneyInfoTransit(
    private val id: Int,
    private val departureTime: String,
    private val arrivalTime: String,
    private val minPrice: Int,
    private val maxPrice: Int) : Serializable {

    companion object {
        fun fromJourneyInfo(journey: JourneyInfo): JourneyInfoTransit {
            return JourneyInfoTransit(
                journey.id,
                journey.departureTime,
                journey.arrivalTime,
                journey.minPrice,
                journey.maxPrice
            )
        }
    }

    fun toJourneyInfo(): JourneyInfo {
        return JourneyInfo(
            this.id,
            this.departureTime,
            this.arrivalTime,
            this.minPrice,
            this.maxPrice
        )
    }
}