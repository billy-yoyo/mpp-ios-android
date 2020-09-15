package com.jetbrains.handson.mpp.mobile.dataclasses

import kotlinx.serialization.Serializable

@Serializable
data class Journey(
    val id: Int,
    val departureTime: String,
    val arrivalTime: String,
    val minPrice: Int,
    val maxPrice: Int
)