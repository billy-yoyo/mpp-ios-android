package com.jetbrains.handson.mpp.mobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


class CustomListAdapter(private val journeys: List<JourneyInfo>, private val context: Context) : RecyclerView.Adapter<CustomListAdapter.CustomViewHolder>() {
    class CustomViewHolder(private val view: View, private val context: Context): RecyclerView.ViewHolder(view) {
        fun bindItems(journey: JourneyInfo) {
            val depTime = view.findViewById<TextView>(R.id.departure_time)
            depTime.text = context.resources.getString(R.string.departure_time, formatDateTime(journey.departureTime))

            val arrTime = view.findViewById<TextView>(R.id.arrival_time)
            arrTime.text = context.resources.getString(R.string.arrival_time, formatDateTime(journey.arrivalTime))

            val prices = view.findViewById<TextView>(R.id.prices)
            prices.text = context.resources.getString(
                R.string.prices,
                journey.minPrice / 100.0,
                journey.maxPrice / 100.0
            )
        }

        private fun formatDateTime(datetime: String): String {
            val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ", Locale.UK)
            val outputFormatter = SimpleDateFormat("MMM d, h:mm a" , Locale.UK)
            val date = inputFormatter.parse(datetime) ?: return datetime
            return outputFormatter.format(date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(journeys[position])
    }

    override fun getItemCount(): Int {
        return journeys.size
    }
}