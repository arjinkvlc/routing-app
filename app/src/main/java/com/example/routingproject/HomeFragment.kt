package com.example.routingproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
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
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class HomeFragment : Fragment(), OnMapReadyCallback {

    private val FINE_PERMISSION_CODE = 1
    private var currentLocation: Location? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: FragmentHomeBinding
    private lateinit var myMap: GoogleMap
    private lateinit var place1: MarkerOptions
    private lateinit var place2: MarkerOptions
    private lateinit var currentPolyLine: PolylineOptions

    private var startLatitudeInput: Double? = null
    private var startLongitudeInput: Double? = null
    private var finishLatitudeInput: Double? = null
    private var finishLongitudeInput: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        startLatitudeInput = arguments?.getDouble("startLatitude")
        startLongitudeInput = arguments?.getDouble("startLongitude")
        finishLatitudeInput = arguments?.getDouble("finishLatitude")
        finishLongitudeInput = arguments?.getDouble("finishLongitude")
        Log.d("test2", startLatitudeInput.toString())

        return binding.root
    }

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
                    val locationString = p0.name ?: ""
                    val position = LatLng(latLng!!.latitude, latLng.longitude)
                    myMap.clear()
                    myMap.addMarker(MarkerOptions().position(position).title(locationString))
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
                }
            }
        })

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
            myMap.addMarker(MarkerOptions().position(position).title("My location"))
            myMap.moveCamera(CameraUpdateFactory.newLatLng(position))

            if (startLatitudeInput != null && finishLatitudeInput != null) {
                val startLatLng = LatLng(startLatitudeInput!!, startLongitudeInput!!)
                val finishLatLng = LatLng(finishLatitudeInput!!, finishLongitudeInput!!)
                place1 = MarkerOptions().position(startLatLng).title("Start")
                place2 = MarkerOptions().position(finishLatLng).title("Destination")
                myMap.addMarker(place1)
                myMap.addMarker(place2)
                val bounds = LatLngBounds.Builder()
                    .include(place1.position)
                    .include(place2.position)
                    .build()

                myMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                // Drawing route
                val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                        "origin=${startLatitudeInput},${startLongitudeInput}&" +
                        "destination=${finishLatitudeInput},${finishLongitudeInput}&" +
                        "key=AIzaSyD5y-blFQL9WPzmfXacOVUXJwerZWWp0gY"
                thread {
                    try {
                        val connection = URL(url).openConnection() as HttpURLConnection
                        val response = connection.inputStream.bufferedReader().use { it.readText() }
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

    private fun decodePoly(encoded: String): PolylineOptions {
        val poly = PolylineOptions()
        poly.color(Color.parseColor("#00E0FF"))
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
