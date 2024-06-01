package com.example.routingproject.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.routingproject.R
import com.example.routingproject.data.model.TripGetAllData
import com.example.routingproject.data.model.TripGetAllResponse
import com.example.routingproject.ui.details.TripDetaisActivityForExplorePage
import org.jsoup.Jsoup

class TripForExplorePageAdapter(val tripList: List<TripGetAllData>) :
    RecyclerView.Adapter<TripForExplorePageAdapter.ExplorePageViewHolder>() {

    inner class ExplorePageViewHolder(private val itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        //Binds the data
        fun bindData(trip: TripGetAllData) {

            val tripConstraintLayout =
                itemView.findViewById<ConstraintLayout>(R.id.trip_constraint_layout)
            val title = itemView.findViewById<TextView>(R.id.trip_item_title_text_view)
            val image = itemView.findViewById<ImageView>(R.id.trip_item_image_view)
            //val likes = itemView.findViewById<TextView>(R.id.trip_item_favorites_text_view)
            val tripTotalTime = itemView.findViewById<TextView>(R.id.trip_item_time_text_view)
            val description = itemView.findViewById<TextView>(R.id.trip_item_description_text_view)


            description.text = Jsoup.parse(trip.tripDescription).text()
            tripTotalTime.text = trip.totalTripTime.toString()
            title.text = trip.tripName

            if (trip.tripName.contains("Antalya") || trip.tripName.contains("antalya")) {
                image.load("https://antalya.com.tr/Uploaded/Content/ac269abe-2a5c-4f57-8ae8-c61aad846792.jpg") {
                }
            }
            else if (trip.tripName.contains("Adiyaman") || trip.tripName.contains("adiyaman")) {
                image.load("https://www.hepsiburadaseyahat.com/blog/wp-content/uploads/2022/09/adiyaman-2-1-scaled.jpg") {
                }
            }
            else if (trip.tripName.contains("Kocaeli") || trip.tripName.contains("kocaeli")) {
                image.load("https://staticb.yolcu360.com/blog/wp-content/uploads/2018/06/23165657/kocaeli.jpg"){

                }
            }
            else if (trip.tripName.contains("İzmir") || trip.tripName.contains("izmir")) {
                image.load("https://trthaberstatic.cdn.wp.trt.com.tr/resimler/2068000/izmir-cesme-iha-2069259.jpg") {
                }
            }
            else if (trip.tripName.contains("Avrupa") || trip.tripName.contains("avrupa")) {
                image.load("https://www.gezigo.com/yuklemeler/tur-gorselleri/25133232_roma_fotoyraf.jpg") {
                }
            }
            else if (trip.tripName.contains("Alanya") || trip.tripName.contains("alanya")) {
                image.load("https://cdn.getyourguide.com/img/tour/643e6b55b00fa.jpeg/146.jpg"){

                }
            }
            else if (trip.tripName.contains("Istanbul") || trip.tripName.contains("istanbul") || trip.tripName.contains("İstanbul")) {
                image.load("https://lp-cms-production.imgix.net/2024-04/-CantoiStock-931220574-RFC.jpg") {
                }
            }
            else if (trip.tripName.contains("Erzurum") || trip.tripName.contains("erzurum")) {
                image.load("https://blog.tatildenince.com/wp-content/uploads/2024/01/erzurum-gezilecek-yerler-800x400.jpg") {
                }
            }
            else if (trip.tripName.contains("Sivas") || trip.tripName.contains("sivas") || trip.tripName.contains("divriği") || trip.tripName.contains("Divriği")) {
                image.load("https://upload.wikimedia.org/wikipedia/commons/d/da/Divrigi%2CKale-S.jpg"){

                }
            }
            else if (trip.tripName.contains("Galatasaray") || trip.tripName.contains("galatasaray")) {
                image.load("https://avatars.mds.yandex.net/get-altay/7695774/2a00000187d203dec23bfbc8d93b1af12277/L_height"){

                }
            }
            else if (trip.tripName.contains("Türkiye") || trip.tripName.contains("türkiye")) {
                image.load("https://www.defensehere.com/img/2021/07/t%C3%BCrkiye-nin-5-nesil-uydusu-t%C3%BCrksat-5a-g%C3%B6reve-ba%C5%9Flad%C4%B1.jpeg"){

                }
            }
            else if (trip.tripName.contains("Güney") || trip.tripName.contains("Güney Amerika")) {
                image.load("https://cdn2.setur.com.tr/image/tour/large/rio-263803_305042.jpg"){

                }
            }
            else if (trip.tripName.contains("Amerika") || trip.tripName.contains("amerika")) {
                image.load("https://cdn.tatilsepeti.com/Files/TurResim/42367/tsr42367638461203649893597.jpg"){

                }
            }

            else{
                image.load("https://lp-cms-production.imgix.net/2024-04/-CantoiStock-931220574-RFC.jpg") {
                }
            }

            //When a trip clicked it navigates to DetailsPage and sends the necessary data
            tripConstraintLayout.setOnClickListener {
                val detailsPage =
                    Intent(itemView.context, TripDetaisActivityForExplorePage::class.java).apply {
                        putExtra("putTitle", trip.tripName)
                        putExtra("putTotalTripTime", trip.totalTripTime)
                        Log.d("tripdetailsadapter", trip.totalTripTime.toString())
                        putParcelableArrayListExtra("putTripStops", ArrayList(trip.stops))
                    }
                itemView.context.startActivity(detailsPage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExplorePageViewHolder {
        return ExplorePageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.trips_row_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ExplorePageViewHolder, position: Int) {
        holder.bindData(tripList[position])
    }

    override fun getItemCount(): Int {
        return tripList.size
    }
}