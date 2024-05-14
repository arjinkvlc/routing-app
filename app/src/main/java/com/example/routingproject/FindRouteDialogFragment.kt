package com.example.routingproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.example.routingproject.databinding.FragmentFindRouteDialogBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class FindRouteDialogFragment : Fragment() {
    private lateinit var binding: FragmentFindRouteDialogBinding
    private lateinit var startInput: LatLng
    private lateinit var finishInput: LatLng

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindRouteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyD5y-blFQL9WPzmfXacOVUXJwerZWWp0gY")
        }
        val autocompleteSupportMapFragmentForStart =
            (childFragmentManager.findFragmentById(R.id.startInputFragment) as AutocompleteSupportFragment)
                .setPlaceFields(
                    listOf(Place.Field.LAT_LNG, Place.Field.NAME)
                )
        autocompleteSupportMapFragmentForStart.setOnPlaceSelectedListener(object :
            PlaceSelectionListener {
            override fun onError(p0: Status) {
                Log.e("error", p0.statusMessage.toString())
            }

            override fun onPlaceSelected(p0: Place) {
                if (p0.latLng != null) {
                    startInput = p0.latLng!!
                }
            }
        })

        val autocompleteSupportMapFragmentForFinish =
            (childFragmentManager.findFragmentById(R.id.finishInputFragment) as AutocompleteSupportFragment)
                .setPlaceFields(
                    listOf(Place.Field.LAT_LNG, Place.Field.NAME)
                )
        autocompleteSupportMapFragmentForFinish.setOnPlaceSelectedListener(object :
            PlaceSelectionListener {
            override fun onError(p0: Status) {
                Log.e("error", p0.statusMessage.toString())
            }

            override fun onPlaceSelected(p0: Place) {
                if (p0.latLng != null) {
                    finishInput = p0.latLng!!
                }
            }
        })

        binding.findRouteButton.setOnClickListener {
            Log.d("test", "${startInput.latitude} - ${finishInput.longitude}")
            val bundle = Bundle().apply {
                putDouble("startLatitude", startInput.latitude)
                putDouble("startLongitude", startInput.longitude)
                putDouble("finishLatitude", finishInput.latitude)
                putDouble("finishLongitude", finishInput.longitude)
            }
            val homeFragment = HomeFragment()
            homeFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.layout, homeFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.backButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .remove(this@FindRouteDialogFragment)
                .commit()

            parentFragmentManager.popBackStack()
        }
    }
}
