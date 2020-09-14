package com.jetbrains.handson.mpp.mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomListAdapter(private val journeys: List<JourneyInfo>) : RecyclerView.Adapter<CustomListAdapter.CustomViewHolder>() {
    class CustomViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        fun bindItems(departureTime: String, arrivalTime: String, maxPrice: Int, minPrice: Int) {
            val colDepTime = view.findViewById<TextView>(R.id.departure_time)
            colDepTime.text = "Departs: " + departureTime.substring(11, 16) // TODO("Use resource string instead")

            val colArrTime = view.findViewById<TextView>(R.id.arrival_time)
            colArrTime.text = "Arrives: " + arrivalTime.substring(11, 16) // TODO("Use resource string instead")

            val colPrices = view.findViewById<TextView>(R.id.prices)
            val strMax: StringBuilder = StringBuilder(maxPrice.toString())
            val strMin = StringBuilder(minPrice.toString())
            strMax.insert(strMax.length - 2, ".")
            strMin.insert(strMin.length - 2, ".")
            colPrices.text = "From £$strMin to £$strMax" // TODO("Use resource string instead")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(
            journeys.map { it.departureTime }[position],
            journeys.map { it.arrivalTime }[position],
            journeys.map { it.maxPrice }[position],
            journeys.map { it.minPrice }[position])
    }

    override fun getItemCount(): Int {
        return journeys.size
    }
}