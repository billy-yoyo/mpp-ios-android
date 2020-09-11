package com.jetbrains.handson.mpp.mobile.models

import kotlinx.serialization.Serializable

@Serializable
data class TicketModel(val name: String, val priceInPennies: Int) {
}