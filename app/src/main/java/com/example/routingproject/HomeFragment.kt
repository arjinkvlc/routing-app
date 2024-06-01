package com.example.routingproject

import SearchHistoryAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.routingproject.data.api.RetrofitClient
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.data.model.SearchHistoryData
import com.example.routingproject.databinding.FragmentHomeBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread
import android.view.ViewTreeObserver


class HomeFragment : Fragment(), OnMapReadyCallback {

    private val FINE_PERMISSION_CODE = 1
    private var currentLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: FragmentHomeBinding
    private lateinit var myMap: GoogleMap
    private lateinit var placeStart: MarkerOptions
    private lateinit var placeDestionation: MarkerOptions
    private lateinit var placeMiddle: MarkerOptions
    private var startLatitudeInput: Double? = null
    private var startLongitudeInput: Double? = null
    private var finishLatitudeInput: Double? = null
    private var finishLongitudeInput: Double? = null
    private var middleLatitudeInput: Double? = null
    private var middleLongitudeInput: Double? = null
    private var middleLatitudeInput2: Double? = null
    private var middleLongitudeInput2: Double? = null
    private var middleLatitudeInput3: Double? = null
    private var middleLongitudeInput3: Double? = null
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private lateinit var searchHistoryRecyclerView: RecyclerView
    private val searchHistoryList = mutableListOf<SearchHistoryData>()
    private lateinit var userRepository: UserRepository
    private lateinit var latitudeList: ArrayList<String>
    private lateinit var longitudeList: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userRepository = UserRepository(RetrofitClient.apiService, requireContext())
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        if (arguments?.getStringArrayList("latitudeList") != null) {
            latitudeList = arguments?.getStringArrayList("latitudeList")!!
            longitudeList = arguments?.getStringArrayList("longitudeList")!!
            Log.d("testlng3", latitudeList.toString())
        }
        searchHistoryRecyclerView = binding.searchHistoryRecyclerView
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(context)
        searchHistoryAdapter = SearchHistoryAdapter(searchHistoryList) { selectedHistory ->
            // Tıklanan öğe için yapılacak işlemler
            Toast.makeText(
                requireContext(),
                "Selected: ${selectedHistory.searchString}",
                Toast.LENGTH_SHORT
            ).show()
        }
        searchHistoryRecyclerView.adapter = searchHistoryAdapter

        startLatitudeInput = arguments?.getDouble("startLatitude")
        startLongitudeInput = arguments?.getDouble("startLongitude")
        finishLatitudeInput = arguments?.getDouble("finishLatitude")
        finishLongitudeInput = arguments?.getDouble("finishLongitude")
        if (arguments?.getDouble("middleLatitude") != null) {
            middleLatitudeInput = arguments?.getDouble("middleLatitude")
            middleLongitudeInput = arguments?.getDouble("middleLongitude")
            placeMiddle = MarkerOptions().position(
                LatLng(
                    middleLatitudeInput!!.toDouble(),
                    middleLongitudeInput!!.toDouble()
                )
            ).title("MidPoint 1")
        }
        if (arguments?.getDouble("middleLatitude2") != null) {
            middleLatitudeInput2 = arguments?.getDouble("middleLatitude2")
            middleLongitudeInput2 = arguments?.getDouble("middleLongitude2")
            placeMiddle = MarkerOptions().position(
                LatLng(
                    middleLatitudeInput2!!.toDouble(),
                    middleLongitudeInput2!!.toDouble()
                )
            ).title("MidPoint 2")
        }
        if (arguments?.getDouble("middleLatitude3") != null) {
            middleLatitudeInput3 = arguments?.getDouble("middleLatitude3")
            middleLongitudeInput3 = arguments?.getDouble("middleLongitude3")
            placeMiddle = MarkerOptions().position(
                LatLng(
                    middleLatitudeInput3!!.toDouble(),
                    middleLongitudeInput3!!.toDouble()
                )
            ).title("MidPoint 3")
        }

        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_PERMISSION_CODE
            )
        } else {
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())
            getLastLocation()
        }

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyD5y-blFQL9WPzmfXacOVUXJwerZWWp0gY")
        }
        val autocompleteSupportMapFragment =
            (childFragmentManager.findFragmentById(R.id.searchFragment) as AutocompleteSupportFragment)
                .setPlaceFields(
                    listOf(Place.Field.LAT_LNG, Place.Field.NAME)
                )


        autocompleteSupportMapFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {}

            override fun onPlaceSelected(p0: Place) {
                if (p0.latLng != null) {
                    val latLng = p0.latLng
                    val locationString = p0.name?.toString() ?: ""
                    val position = LatLng(latLng!!.latitude, latLng.longitude)
                    myMap.clear()
                    myMap.addMarker(MarkerOptions().position(position).title(locationString))
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
                    lifecycleScope.launch {
                        try {
                            Log.d(
                                "testsearch",
                                locationString + userRepository.toString() + locationString
                            )
                            userRepository.postSearch(locationString)
                        } catch (e: Exception) {
                            Log.e("API_ERROR", "Exception: ${e.message}")

                        }
                    }
                }
            }
        })
        binding.historyButton.setOnClickListener {
            // Arama geçmişini yükle ve görünür yap
            Log.d("testhistory", "visible")
            fetchSearchHistory()
            if (binding.historyButton.isVisible)
                binding.historyButton.visibility = View.GONE
            binding.routeButton.visibility = View.GONE
            binding.searchHistoryBackButton.visibility = View.VISIBLE
        }
        binding.searchHistoryBackButton.setOnClickListener {
            searchHistoryRecyclerView.visibility = View.GONE
            binding.searchHistoryBackButton.visibility = View.GONE
            binding.historyButton.visibility = View.VISIBLE
            binding.routeButton.visibility = View.VISIBLE
        }

        binding.routeButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.layout, FindRouteDialogFragment())
                .addToBackStack(null)
                .commit()
            binding.routeButton.visibility = View.GONE
        }

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun fetchSearchHistory() {

        lifecycleScope.launch {
            try {
                val response = userRepository.getSearchHistory(1, 50)
                if (response.succeeded) {
                    searchHistoryList.clear()
                    searchHistoryList.addAll(response.data)
                    searchHistoryAdapter.notifyDataSetChanged()

                    // RecyclerView'i görünür yap
                    searchHistoryRecyclerView.visibility = View.VISIBLE
                } else {
                    Log.e("API_ERROR", "Failed to fetch search history: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Exception: ${e.message}")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (currentLocation == null) {
            val task: Task<Location> = fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener {
                currentLocation = it
                currentLocation?.let {
                    val mapFragment: SupportMapFragment =
                        childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                    mapFragment.getMapAsync(this)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Location permission is denied, please allow the permission.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap

        currentLocation?.let {
            val position = LatLng(it.latitude, it.longitude)
            myMap.clear()
            myMap.addMarker(MarkerOptions().position(position).title("My location"))
            myMap.moveCamera(CameraUpdateFactory.newLatLng(position))
            //Routing Event
            if (startLatitudeInput != null && finishLatitudeInput != null) {
                if (startLatitudeInput != 0.0 && finishLatitudeInput != 0.0) {
                    Log.d("testlng5", "route kısmı null değil" + startLatitudeInput)
                    val startLatLng = LatLng(startLatitudeInput!!, startLongitudeInput!!)
                    val finishLatLng = LatLng(finishLatitudeInput!!, finishLongitudeInput!!)
                    placeStart = MarkerOptions().position(startLatLng).title("Start")
                    placeDestionation = MarkerOptions().position(finishLatLng).title("Destination")
                    myMap.addMarker(placeStart)
                    myMap.addMarker(placeDestionation)

                    val boundsBuilder = LatLngBounds.Builder().include(placeStart.position)
                    if (middleLatitudeInput != 0.0 && middleLongitudeInput != 0.0) {
                        val middleLatLng = LatLng(middleLatitudeInput!!, middleLongitudeInput!!)
                        val placeMiddle = MarkerOptions().position(middleLatLng).title("MidPoint 1")
                        myMap.addMarker(placeMiddle)
                        boundsBuilder.include(placeMiddle.position)
                    }
                    if (middleLatitudeInput2 != 0.0 && middleLongitudeInput2 != 0.0) {
                        val middleLatLng2 = LatLng(middleLatitudeInput2!!, middleLongitudeInput2!!)
                        val placeMiddle2 =
                            MarkerOptions().position(middleLatLng2).title("MidPoint 2")
                        myMap.addMarker(placeMiddle2)
                        boundsBuilder.include(placeMiddle2.position)
                    }
                    if (middleLatitudeInput3 != 0.0 && middleLongitudeInput3 != 0.0) {
                        val middleLatLng3 = LatLng(middleLatitudeInput3!!, middleLongitudeInput3!!)
                        val placeMiddle3 =
                            MarkerOptions().position(middleLatLng3).title("MidPoint 3")
                        myMap.addMarker(placeMiddle3)
                        boundsBuilder.include(placeMiddle3.position)
                    }

                    boundsBuilder.include(placeDestionation.position)
                    val bounds = boundsBuilder.build()

                    // Harita yerleşiminin tamamlanmasını bekleyin
                    val mapView =
                        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).view
                    mapView?.viewTreeObserver?.addOnGlobalLayoutListener(object :
                        ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            mapView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            myMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                        }
                    })

                    // Drawing route
                    val urlRoute = "https://maps.googleapis.com/maps/api/directions/json?" +
                            "origin=${startLatitudeInput},${startLongitudeInput}&" +
                            "destination=${finishLatitudeInput},${finishLongitudeInput}" +
                            // Ara noktalar için kontrol ekleyin
                            if (middleLongitudeInput != null && middleLongitudeInput != 0.0) {
                                "&waypoints=${middleLatitudeInput},${middleLongitudeInput}"
                            } else {
                                ""
                            } +
                            if (middleLongitudeInput2 != null && middleLongitudeInput2 != 0.0) {
                                "|${middleLatitudeInput2},${middleLongitudeInput2}"
                            } else {
                                ""
                            } +
                            if (middleLongitudeInput3 != null && middleLongitudeInput3 != 0.0) {
                                "|${middleLatitudeInput3},${middleLongitudeInput3}"
                            } else {
                                ""
                            } +
                            "&key=AIzaSyD5y-blFQL9WPzmfXacOVUXJwerZWWp0gY"
                    Log.d("testurlRoute", urlRoute)

                    thread {
                        try {
                            val connection = URL(urlRoute).openConnection() as HttpURLConnection
                            val response =
                                connection.inputStream.bufferedReader().use { it.readText() }
                            val json = JSONObject(response)
                            val routes = json.getJSONArray("routes")
                            val route = routes.getJSONObject(0)
                            val polyline = route.getJSONObject("overview_polyline")
                            val points = polyline.getString("points")
                            val line = decodePoly(points)
                            requireActivity().runOnUiThread {
                                myMap.addPolyline(line)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            //Trip Event
            if (arguments?.getStringArrayList("latitudeList") != null) {
                if (latitudeList.size > 1) {
                    myMap.clear()
                    val startLatLng =
                        LatLng(latitudeList[0].toDouble(), longitudeList[0].toDouble())
                    val finishLatLng = LatLng(
                        latitudeList[latitudeList.size - 1].toDouble(),
                        longitudeList[longitudeList.size - 1].toDouble()
                    )
                    currentLocation?.let {
                        if (startLatLng.latitude != 0.0 && startLatLng.longitude != 0.0 && finishLatLng.latitude != 0.0 && finishLatLng.longitude != 0.0) {
                            Log.d("testlng4", startLatLng.toString() + finishLatLng.toString())
                            placeStart = MarkerOptions().position(startLatLng).title("Start")
                            placeDestionation =
                                MarkerOptions().position(finishLatLng).title("Destination")
                            myMap.addMarker(placeStart)
                            myMap.addMarker(placeDestionation)
                            val boundsBuilder = LatLngBounds.Builder().include(placeStart.position)
                            var urlWaypoints = ""
                            for (i in 1 until latitudeList.size - 1) {
                                Log.d("testtest", latitudeList[i])
                                val place = MarkerOptions().position(
                                    LatLng(
                                        latitudeList[i].toDouble(),
                                        longitudeList[i].toDouble()
                                    )
                                ).title(("Waypoint " + i))
                                myMap.addMarker(place)
                                boundsBuilder.include((place.position))
                                urlWaypoints += latitudeList[i] + "," + longitudeList[i] + "|"
                            }
                            Log.d("urlWaypoints", urlWaypoints)
                            boundsBuilder.include(placeDestionation.position)
                            val bounds = boundsBuilder.build()


                            // Harita yerleşiminin tamamlanmasını bekleyin
                            val mapView =
                                (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).view
                            mapView?.viewTreeObserver?.addOnGlobalLayoutListener(object :
                                ViewTreeObserver.OnGlobalLayoutListener {
                                override fun onGlobalLayout() {
                                    mapView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                                    myMap.animateCamera(
                                        CameraUpdateFactory.newLatLngBounds(
                                            bounds,
                                            100
                                        )
                                    )
                                }
                            })

                            val urlTrip = "https://maps.googleapis.com/maps/api/directions/json?" +
                                    "origin=" + startLatLng.latitude + "," + startLatLng.longitude +
                                    "&waypoints=" + urlWaypoints +
                                    "&destination=" + finishLatLng.latitude + "," + finishLatLng.longitude +
                                    "&key=AIzaSyD5y-blFQL9WPzmfXacOVUXJwerZWWp0gY"
                            Log.d("testurlTrip", urlTrip)
                            thread {
                                try {
                                    val connection =
                                        URL(urlTrip).openConnection() as HttpURLConnection
                                    val response = connection.inputStream.bufferedReader()
                                        .use { it.readText() }
                                    val json = JSONObject(response)
                                    val routes = json.getJSONArray("routes")
                                    val route = routes.getJSONObject(0)
                                    val polyline = route.getJSONObject("overview_polyline")
                                    val points = polyline.getString("points")
                                    val line = decodePoly(points)
                                    requireActivity().runOnUiThread {
                                        myMap.addPolyline(line)
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
            }
        }


    }

    private fun decodePoly(encoded: String): PolylineOptions {
        val poly = PolylineOptions()
        poly.color(Color.parseColor("#007BFF"))
        poly.width(15f)
        val len = encoded.length
        var index = 0
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
            poly.add(p)
        }
        return poly
    }
}