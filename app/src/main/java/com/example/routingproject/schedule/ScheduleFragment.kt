package com.example.routingproject.schedule

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.routingproject.R
import com.example.routingproject.adapter.ScheduleAdapter
import com.example.routingproject.data.api.RetrofitClient
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.data.model.AddTripRequest
import com.example.routingproject.data.model.TripGetAllData
import com.example.routingproject.databinding.FragmentScheduleBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ScheduleFragment : Fragment(), AddScheduleDialogFragment.AddScheduleDialogListener {

    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var binding: FragmentScheduleBinding
    private lateinit var tripGetAllData: List<TripGetAllData>
    private lateinit var recyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentScheduleBinding.inflate(inflater,container,false)

        val userRepository = UserRepository(RetrofitClient.apiService,requireContext())

        scheduleViewModel = ViewModelProvider(
            this,
            ScheduleViewModelFactory(userRepository,requireContext())
        )[ScheduleViewModel::class.java]

        lifecycleScope.launch {
            async {
                tripGetAllData = scheduleViewModel.getTrips(requireContext())
                recyclerView = binding.recyclerviewList
                scheduleAdapter = ScheduleAdapter(tripGetAllData,this@ScheduleFragment)
                recyclerView.adapter=scheduleAdapter
                recyclerView.layoutManager=LinearLayoutManager(activity)
            }
        }


        binding.addButton.setOnClickListener {
            val dialog = AddScheduleDialogFragment()
            dialog.setListener(this)
            dialog.show(childFragmentManager, "com.example.routingproject.schedule.AddScheduleDialogFragment")
        }

        return binding.root
    }

    override fun onDialogPositiveClick(tripTitle: String, tripDescription: String, tripStartTime: String) {
        val userRepository = UserRepository(RetrofitClient.apiService,requireContext())
        scheduleViewModel = ViewModelProvider(
            this,
            ScheduleViewModelFactory(userRepository, requireContext())
        )[ScheduleViewModel::class.java]
        lifecycleScope.launch {
            if(!tripTitle.equals(null) && !tripDescription.equals(null) && !tripStartTime.equals(null)){
                try {
                    val addTripRequest=AddTripRequest(false,tripDescription,tripTitle,tripStartTime)
                    scheduleViewModel.addTrip(addTripRequest)
                    restartFragment()
                    Toast.makeText(requireContext(),"Trip Added Successfully",Toast.LENGTH_SHORT).show()
                }catch (e: Exception){
                    Toast.makeText(requireContext(), "Trip info cannot be null", Toast.LENGTH_LONG).show()
                }
            }

        }
    }
    private fun restartFragment() {
        parentFragmentManager.beginTransaction().detach(this).commit()
        parentFragmentManager.beginTransaction().attach(this).commit()
    }
}
class ScheduleViewModelFactory(private val userRepository: UserRepository,private val context:Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            return ScheduleViewModel(userRepository,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
