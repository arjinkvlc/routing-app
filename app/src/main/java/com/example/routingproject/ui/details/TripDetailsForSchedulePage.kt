package com.example.routingproject.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.routingproject.HomePage
import com.example.routingproject.R
import com.example.routingproject.adapter.TripDetailsForScheduleAdapter
import com.example.routingproject.data.api.RetrofitClient
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.data.model.AddLocationRequest
import com.example.routingproject.data.model.Stop
import com.example.routingproject.data.model.StopRequest
import com.example.routingproject.databinding.ActivityTripDetailsForSchedulePageBinding
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class TripDetailsForSchedulePage : AppCompatActivity() {
    lateinit var binding: ActivityTripDetailsForSchedulePageBinding
    private lateinit var tripDetailsAdapter: TripDetailsForScheduleAdapter
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripDetailsForSchedulePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userRepository = UserRepository(RetrofitClient.apiService, this)
        binding.tripTitleDetailsEditText.text = intent.getStringExtra("putTitle")
        val tripTotalTime = intent.getIntExtra("putTotalTripTime", 0)
        val latitudeList: ArrayList<String> = arrayListOf()
        val longitudeList: ArrayList<String> = arrayListOf()
        val stopsData = intent.getParcelableArrayListExtra<Stop>("putTripStops")
        val locationIds = extractLocationIds(stopsData.toString())
        Log.d("stops2", locationIds.toString())
        val tripId = intent.getIntExtra("putTripId", 0)

        tripDetailsAdapter = stopsData?.let { TripDetailsForScheduleAdapter(it.toMutableList()) }!!
        binding.recyclerviewList.layoutManager = LinearLayoutManager(this)
        binding.recyclerviewList.adapter = tripDetailsAdapter

        binding.tripTimeNumber.text = tripTotalTime.toString()
        binding.detailsPageBackButton.setOnClickListener {
            finish()
        }

        if (!Places.isInitialized()) {
            Places.initialize(this, "AIzaSyD5y-blFQL9WPzmfXacOVUXJwerZWWp0gY")
        }
        val autocompleteSupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.addLocationFragment) as AutocompleteSupportFragment?
        autocompleteSupportMapFragment?.setPlaceFields(
            listOf(Place.Field.LAT_LNG, Place.Field.NAME)
        )
        autocompleteSupportMapFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {
                Log.e("error", p0.statusMessage.toString())
            }

            override fun onPlaceSelected(p0: Place) {
                if (p0.latLng != null) {
                    lifecycleScope.launch {
                        try {
                            val addLocationRequest = AddLocationRequest(
                                p0.latLng.latitude,
                                p0.address ?: "",
                                p0.name,
                                p0.latLng.longitude
                            )
                            val locationId = userRepository.addLocation(addLocationRequest)
                            val stopRequest = StopRequest(
                                getCurrentDateTimeFormatted(),
                                locationId,
                                stopsData.size + 1,
                                getCurrentDateTimeFormatted(),
                                p0.name,
                                tripId,
                                60
                            )
                            userRepository.postStop(stopRequest)
                            updateAdapterData(tripId)
                        } catch (e: Exception) {
                            Log.e("ERROR", "Failed to add new stop: ${e.message}")
                        }
                    }
                }
            }
        })

        binding.roueStopsScheduleButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    for (location in locationIds) {
                        latitudeList.add(userRepository.getLocationById(location).data.latitude.toString())
                        longitudeList.add(userRepository.getLocationById(location).data.longitude.toString())
                    }
                    changeActivity(latitudeList, longitudeList)
                } catch (e: Exception) {
                    Log.e("ERROR", "Failed to fetch location data: ${e.message}")
                }
            }
        }
    }

    fun extractLocationIds(input: String): List<Int> {
        val locationIdRegex = """locationId=(\d+)""".toRegex()
        val matches = locationIdRegex.findAll(input)
        return matches.map { it.groupValues[1].toInt() }.toList()
    }

    private fun changeActivity(latitudeList: ArrayList<String>, longitudeList: ArrayList<String>) {
        val intent = Intent(this, HomePage::class.java)
        intent.putStringArrayListExtra("latitudeList", ArrayList(latitudeList))
        intent.putStringArrayListExtra("longitudeList", ArrayList(longitudeList))
        startActivity(intent)
    }

    private fun getCurrentDateTimeFormatted(): String {
        val calendar = Calendar.getInstance() // Mevcut zamanı alır
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(calendar.time)
    }

    private fun updateAdapterData(tripId: Int) {
        lifecycleScope.launch {
            try {
                val tripResponse = userRepository.getTrips(1, 100)
                val stops = tripResponse.data.find { it.tripId == tripId }?.stops ?: listOf()
                tripDetailsAdapter.updateStops(stops.toMutableList())
            } catch (e: Exception) {
                Log.e("ERROR", "Failed to update adapter data: ${e.message}")
            }
        }
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        return super.onCreateView(parent, name, context, attrs)
    }
}
