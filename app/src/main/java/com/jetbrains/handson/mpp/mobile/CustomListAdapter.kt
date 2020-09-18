package com.jetbrains.handson.mpp.mobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jetbrains.handson.mpp.mobile.dataclasses.Journey
import com.jetbrains.handson.mpp.mobile.util.DateFormatHelper


class CustomListAdapter(
    private val journeys: List<Journey>,
    private val context: Context
) : RecyclerView.Adapter<CustomListAdapter.CustomViewHolder>() {
    private var listener: ((item: Journey) -> Unit)? = null

    fun setOnItemClickListener(listener: (item: Journey) -> Unit) {
        this.listener = listener
    }

    class CustomViewHolder(private val view: View, private val context: Context): RecyclerView.ViewHolder(view) {
        fun bindItems(journey: Journey) {
            val departureTime = view.findViewById<TextView>(R.id.departure_time)
            departureTime.text = context.resources.getString(
                R.string.departure_time,
                DateFormatHelper.formatDateTime(journey.departureTime)
            )

            val arrivalTime = view.findViewById<TextView>(R.id.arrival_time)
            arrivalTime.text = context.resources.getString(
                R.string.arrival_time,
                DateFormatHelper.formatDateTime(journey.arrivalTime)
            )

            val prices = view.findViewById<TextView>(R.id.prices)

            if (journey.minPrice == journey.maxPrice) {
                prices.text = context.resources.getString(
                    R.string.single_price,
                    journey.minPrice / 100.0
                )
            } else {
                prices.text = context.resources.getString(
                    R.string.prices,
                    journey.minPrice / 100.0,
                    journey.maxPrice / 100.0
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(journeys[position])
        holder.itemView.setOnClickListener{
            listener?.invoke(journeys[position])
        }
    }

    override fun getItemCount(): Int {
        return journeys.size
    }
}