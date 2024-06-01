package com.example.routingproject


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
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
    private var middleInput1: LatLng? = null
    private var middleInput2: LatLng? = null
    private var middleInput3: LatLng? = null
    private var midpointCounter:Int=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        midpointCounter=0
        binding = FragmentFindRouteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyD5y-blFQL9WPzmfXacOVUXJwerZWWp0gY")
        }
        //Startpoint autocomplete
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
        //Endpoint autocomplete
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

        //MiddlePoint1 Autocomplete
        val autocompleteSupportMapFragmentForMiddle =
            (childFragmentManager.findFragmentById(R.id.middleInputFragment) as AutocompleteSupportFragment)
                .setPlaceFields(
                    listOf(Place.Field.LAT_LNG, Place.Field.NAME)
                )
        autocompleteSupportMapFragmentForMiddle.setOnPlaceSelectedListener(object :
            PlaceSelectionListener {
            override fun onError(p0: Status) {
                Log.e("error", p0.statusMessage.toString())
            }

            override fun onPlaceSelected(p0: Place) {
                if (p0.latLng != null) {
                    middleInput1 = p0.latLng!!
                }
            }
        })
        //MiddlePoint2 Autocomplete
        val autocompleteSupportMapFragmentForMiddle2 =
            (childFragmentManager.findFragmentById(R.id.middleInputFragment2) as AutocompleteSupportFragment)
                .setPlaceFields(
                    listOf(Place.Field.LAT_LNG, Place.Field.NAME)
                )
        autocompleteSupportMapFragmentForMiddle2.setOnPlaceSelectedListener(object :
            PlaceSelectionListener {
            override fun onError(p0: Status) {
                Log.e("error", p0.statusMessage.toString())
            }

            override fun onPlaceSelected(p0: Place) {
                if (p0.latLng != null) {
                    middleInput2 = p0.latLng!!
                }
            }
        })
        //MiddlePoint3 Autocomplete
        val autocompleteSupportMapFragmentForMiddle3 =
            (childFragmentManager.findFragmentById(R.id.middleInputFragment3) as AutocompleteSupportFragment)
                .setPlaceFields(
                    listOf(Place.Field.LAT_LNG, Place.Field.NAME)
                )
        autocompleteSupportMapFragmentForMiddle3.setOnPlaceSelectedListener(object :
            PlaceSelectionListener {
            override fun onError(p0: Status) {
                Log.e("error", p0.statusMessage.toString())
            }

            override fun onPlaceSelected(p0: Place) {
                if (p0.latLng != null) {
                    middleInput3 = p0.latLng!!
                }
            }
        })

        binding.backButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .remove(this@FindRouteDialogFragment)
                .commit()
            parentFragmentManager.popBackStack()
        }
        binding.addMidPointButton.setOnClickListener{
                midpointCounter++
            if (midpointCounter==1){
                binding.middlePointLayout.visibility=View.VISIBLE
            }else if (midpointCounter==2){
                binding.middlePointLayout2.visibility=View.VISIBLE
            }else if (midpointCounter==3){
                binding.middlePointLayout3.visibility=View.VISIBLE
            }else{
                midpointCounter=3
                Toast.makeText(requireActivity(),"You can add 3 midpoints at most!",Toast.LENGTH_SHORT).show()
            }
        }
        binding.removeMiddlePointButton.setOnClickListener{
            if (binding.middlePointLayout3.isVisible){
                binding.middlePointLayout3.visibility=View.GONE
                middleInput3=null
                midpointCounter--
                return@setOnClickListener
            }else if (binding.middlePointLayout2.isVisible) {
                binding.middlePointLayout2.visibility = View.GONE
                middleInput2=null
                midpointCounter--
                return@setOnClickListener
            }else if (binding.middlePointLayout.isVisible) {
                binding.middlePointLayout.visibility = View.GONE
                middleInput1=null
                midpointCounter--
                return@setOnClickListener
            }else{
                midpointCounter=0
            }
        }
        binding.findRouteButton.setOnClickListener {
            Log.d("test", "${startInput.latitude} - ${finishInput.longitude}")
            val bundle = Bundle().apply {
                putDouble("startLatitude", startInput.latitude)
                putDouble("startLongitude", startInput.longitude)
                putDouble("finishLatitude", finishInput.latitude)
                putDouble("finishLongitude", finishInput.longitude)
                if (middleInput1!=null){
                    Log.d("test1",middleInput1.toString())
                    putDouble("middleLatitude", middleInput1!!.latitude)
                    putDouble("middleLongitude", middleInput1!!.longitude)
                }
                if (middleInput2!=null){
                    Log.d("test2",middleInput2.toString())
                    putDouble("middleLatitude2", middleInput2!!.latitude)
                    putDouble("middleLongitude2", middleInput2!!.longitude)
                }
                if (middleInput3!=null){
                    Log.d("test3",middleInput3.toString())
                    putDouble("middleLatitude3", middleInput3!!.latitude)
                    putDouble("middleLongitude3", middleInput3!!.longitude)
                }
            }
            val homeFragment = HomeFragment()
            homeFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.layout, homeFragment)
                .addToBackStack(null)
                .commit()
        }
    }

}
