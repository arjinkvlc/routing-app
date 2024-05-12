package com.example.routingproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task

class HomeFragment : Fragment() {
    private val FINE_PERMISSION_CODE=1;
    private var currentLocation: Location? = null
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private val callback = OnMapReadyCallback { googleMap ->
        currentLocation?.let {
            val position = LatLng(it.latitude, it.longitude)
            googleMap.addMarker(MarkerOptions().position(position).title("My location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position))
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_PERMISSION_CODE)
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()

        return inflater.
        inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
         val task: Task<Location> = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            currentLocation=it
             val mapFragment:SupportMapFragment=childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
             mapFragment.getMapAsync(callback)
         }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    @Override
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==FINE_PERMISSION_CODE){
            if (grantResults.size >0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }else{
                Toast.makeText(requireActivity(),"Location permission is denied,please allow the permission.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
