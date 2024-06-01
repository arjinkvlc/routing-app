package com.example.routingproject.adapter

import android.content.Intent
import android.util.Log
import com.example.routingproject.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.routingproject.data.api.RetrofitClient
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.data.model.TripGetAllData
import com.example.routingproject.ui.details.TripDetailsForSchedulePage
import com.example.routingproject.ui.details.TripDetaisActivityForExplorePage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup


class FavoritesAdapter(private val itemList: List<TripGetAllData>) :
    RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {
    private var starCheck=false

    inner class FavoritesViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Binds the data
        @OptIn(DelicateCoroutinesApi::class)
        fun bindData(trip: TripGetAllData) {
            val userRepository = UserRepository(RetrofitClient.apiService, itemView.context)
            val title = itemView.findViewById<TextView>(R.id.title_text_view)
            val tripTotalTime = itemView.findViewById<TextView>(R.id.time_text_view)
            val description = itemView.findViewById<TextView>(R.id.description_text_view)
            val deleteButton = itemView.findViewById<ImageButton>(R.id.deleteButton)
            description.text = Jsoup.parse(trip.tripDescription).text()
            tripTotalTime.text = trip.totalTripTime.toString()
            val isFavourite=trip.isFavourite
            val favButton=itemView.findViewById<ImageView>(R.id.favorites_image_view)
            if (isFavourite){
                favButton.setBackgroundResource(R.drawable.baseline_star_rate_24)
            }else{
                favButton.setBackgroundResource(R.drawable.baseline_star_outline_24)
            }
            title.text = trip.tripName
            val tripId = trip.tripId
            deleteButton.setOnClickListener {
                GlobalScope.launch {
                    userRepository.deleteTripById(tripId)
                }
            }
            val tripItem=itemView.findViewById<ConstraintLayout>(R.id.getTripConstraintLayout)
            tripItem.setOnClickListener{
                val detailsPage = Intent(itemView.context,
                    TripDetailsForSchedulePage::class.java).apply {
                    putExtra("putTitle", trip.tripName)
                    putExtra("putTotalTripTime", trip.totalTripTime)
                    putParcelableArrayListExtra("putTripStops", ArrayList(trip.stops))
                    putExtra("putTripId",tripId)
                }
                itemView.context.startActivity(detailsPage)

            }
            favButton.setOnClickListener{
                starCheck=!starCheck
                if (starCheck){
                    favButton.setBackgroundResource(R.drawable.baseline_star_rate_24)
                    GlobalScope.launch {
                        userRepository.changeFavoriteFlagForTrip(tripId,starCheck)
                    }
                }else{
                    favButton.setBackgroundResource(R.drawable.baseline_star_outline_24)
                    GlobalScope.launch {
                        userRepository.changeFavoriteFlagForTrip(tripId,starCheck)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_trip, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bindData(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}