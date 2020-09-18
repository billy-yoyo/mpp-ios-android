package com.jetbrains.handson.mpp.mobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jetbrains.handson.mpp.mobile.dataclasses.Ticket

class CustomJourneyAdapter(
    private val tickets: List<Ticket>,
    private val context: Context
) : RecyclerView.Adapter<CustomJourneyAdapter.CustomViewHolder>() {
    class CustomViewHolder(private val view: View, private val context: Context): RecyclerView.ViewHolder(view) {
        fun bindItems(ticket: Ticket) {
            val ticketType = view.findViewById<TextView>(R.id.ticket_type)
            ticketType.text = ticket.name

            val price = view.findViewById<TextView>(R.id.ticket_price)
            price.text = context.resources.getString(R.string.ticket_price, ticket.price / 100.0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ticket_list_item, parent, false)
        return CustomViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(tickets[position])
    }

    override fun getItemCount(): Int {
        return tickets.size
    }
}