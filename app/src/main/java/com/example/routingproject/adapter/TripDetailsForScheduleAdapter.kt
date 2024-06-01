package com.example.routingproject.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.routingproject.R
import com.example.routingproject.data.api.RetrofitClient
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.data.model.Stop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TripDetailsForScheduleAdapter(private var stopsList: MutableList<Stop>) :
    RecyclerView.Adapter<TripDetailsForScheduleAdapter.TripDetailsViewHolder>() {
    private val locationsList = mutableListOf<String>()

    inner class TripDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_text_view)
        val description: TextView = itemView.findViewById(R.id.description_text_view)
        val timeTextView: TextView = itemView.findViewById(R.id.time_text_view)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        val userRepository = UserRepository(RetrofitClient.apiService, itemView.context)

        fun bindData(stop: Stop) {
            title.text = stop.stopName
            description.text = stop.stopDescription
            timeTextView.text = stop.spendingTime.toString()
            locationsList.add(stop.stopDescription)
            Log.d("teststoplocation", locationsList.toString())
            deleteButton.setOnClickListener {
                for (itemName in stopsList) {
                    if (title.text == itemName.stopName) {
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                userRepository.deleteStopById(itemName.stopId)
                                // Remove item from the list and notify adapter
                                val position = stopsList.indexOf(itemName)
                                stopsList.removeAt(position)
                                notifyItemRemoved(position)
                            } catch (e: Exception) {
                                Log.e("ERROR", "Failed to delete stop: ${e.message}")
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripDetailsViewHolder {
        return TripDetailsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_stop_schedule, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TripDetailsViewHolder, position: Int) {
        holder.bindData(stopsList[position])
    }

    override fun getItemCount(): Int {
        return stopsList.size
    }

    fun updateStops(newStops: MutableList<Stop>) {
        stopsList = newStops
        notifyDataSetChanged()
    }
}
