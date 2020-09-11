package com.jetbrains.handson.mpp.mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomListAdapter(private val data: List<String>): RecyclerView.Adapter<CustomListAdapter.CustomViewHolder>() {
    class CustomViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        fun bindItems(item: String, position: Int) {
            val number = view.findViewById<TextView>(R.id.item_number)
            val label = view.findViewById<TextView>(R.id.item_label)
            number.text = position.toString()
            label.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(data[position], position + 1)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}