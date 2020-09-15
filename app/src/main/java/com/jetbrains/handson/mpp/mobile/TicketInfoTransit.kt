package com.jetbrains.handson.mpp.mobile

import java.io.Serializable

class TicketInfoTransit(
    private val name: String,
    private val description: String,
    private val price: Int) : Serializable {

    companion object {
        fun fromTicketInfo(ticket: TicketInfo): TicketInfoTransit {
            return TicketInfoTransit(
                ticket.name,
                ticket.description,
                ticket.price
            )
        }
    }

    fun toTicketInfo(): TicketInfo {
        return TicketInfo(
            this.name,
            this.description,
            this.price
        )
    }
}