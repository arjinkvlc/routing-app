package com.example.routingproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.routingproject.R
import com.example.routingproject.data.model.Stop

class TripDetailsForExploreAdapter(val stopsList: List<Stop>) : RecyclerView.Adapter<TripDetailsForExploreAdapter.TripDetailsViewHolder>() {

    inner class TripDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_text_view)
        val description: TextView = itemView.findViewById(R.id.description_text_view)
        val timeTextView: TextView = itemView.findViewById(R.id.time_text_view)

        fun bindData(stop: Stop) {
            title.text = stop.stopName
            description.text = stop.stopDescription
            timeTextView.text = stop.spendingTime.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripDetailsViewHolder {
        return TripDetailsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_stop_explore, parent, false))
    }

    override fun onBindViewHolder(holder: TripDetailsViewHolder, position: Int) {
        holder.bindData(stopsList[position])
    }

    override fun getItemCount(): Int {
        return stopsList.size
    }
}