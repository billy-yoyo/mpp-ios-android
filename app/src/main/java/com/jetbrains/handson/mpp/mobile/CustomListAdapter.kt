package com.jetbrains.handson.mpp.mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomListAdapter(private val tickets: List<TicketInfo>) : RecyclerView.Adapter<CustomListAdapter.CustomViewHolder>() {
    class CustomViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        fun bindItems(departureTime: String, arrivalTime: String, maxPrice: Int, minPrice: Int) {
            val colDepTime = view.findViewById<TextView>(R.id.departure_time)
            colDepTime.text = "Departs: " + departureTime.substring(11, 16)

            val colArrTime = view.findViewById<TextView>(R.id.arrival_time)
            colArrTime.text = "Arrives: " + arrivalTime.substring(11, 16)

            val colMaxPrice = view.findViewById<TextView>(R.id.max_price)
            val strMax: StringBuilder = StringBuilder(maxPrice.toString())
            strMax.insert(strMax.length - 2, ".")
            colMaxPrice.text = "£$strMax"

            val colMinPrice = view.findViewById<TextView>(R.id.min_price)
            val strMin = StringBuilder(minPrice.toString())
            strMin.insert(strMin.length - 2, ".")
            colMinPrice.text = "£$strMin"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(
            tickets.map { it.departureTime }[position],
            tickets.map { it.arrivalTime }[position],
            tickets.map { it.maxPrice }[position],
            tickets.map { it.minPrice }[position])
    }

    override fun getItemCount(): Int {
        return tickets.size
    }
}