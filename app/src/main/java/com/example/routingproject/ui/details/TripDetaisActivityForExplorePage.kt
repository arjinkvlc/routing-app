package com.example.routingproject.ui.details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.routingproject.HomePage
import com.example.routingproject.adapter.TripDetailsForExploreAdapter
import com.example.routingproject.data.api.RetrofitClient
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.data.model.Stop
import com.example.routingproject.databinding.ActivityTripDetaisForExplorePageBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TripDetaisActivityForExplorePage : AppCompatActivity() {
    lateinit var binding: ActivityTripDetaisForExplorePageBinding
    private lateinit var tripDetailsAdapter: TripDetailsForExploreAdapter
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTripDetaisForExplorePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = UserRepository(RetrofitClient.apiService, this)

        val tripName = intent.getStringExtra("putTitle")
        val tripTotalTime = intent.getIntExtra("putTotalTripTime", 0)
        val stopsData = intent.getParcelableArrayListExtra<Stop>("putTripStops")

        val latitudeList :ArrayList<String> = arrayListOf()
        val longitudeList :ArrayList<String> = arrayListOf()

        val locationIds = extractLocationIds(stopsData.toString())

        binding.tripTitleDetailsExploreEditText.text = intent.getStringExtra("putTitle")

        Log.d("tripdetailsactivity", stopsData.toString())
        Log.d("tripdetailsactivity", tripName.toString())
        Log.d("tripdetailsactivity", tripTotalTime.toString())

        tripDetailsAdapter = stopsData?.let { TripDetailsForExploreAdapter(it) }!!
        binding.recyclerviewList.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewList.adapter = tripDetailsAdapter

        binding.tripTimeNumber.text = tripTotalTime.toString()
        binding.detailsPageBackButton.setOnClickListener {
            finish()
        }

        binding.roueStopsScheduleButton.setOnClickListener {
            lifecycleScope.launch {
                async {
                    for (location in locationIds) {
                        latitudeList.add(userRepository.getLocationById(location).data.latitude.toString())
                        longitudeList.add(userRepository.getLocationById(location).data.longitude.toString())
                    }
                    changeActivity(latitudeList, longitudeList)
                }
            }

        }

    }

    private fun changeActivity(latitudeList:ArrayList<String>,longitudeList:ArrayList<String>){
        val intent = Intent(this, HomePage::class.java)
        intent.putStringArrayListExtra("latitudeList", ArrayList(latitudeList))
        intent.putStringArrayListExtra("longitudeList", ArrayList(longitudeList))
        startActivity(intent)
    }

    fun extractLocationIds(input: String): List<Int> {
        val locationIdRegex = """locationId=(\d+)""".toRegex()
        val matches = locationIdRegex.findAll(input)
        return matches.map { it.groupValues[1].toInt() }.toList()
    }

}