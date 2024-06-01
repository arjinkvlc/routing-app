package com.example.routingproject.ui.explore

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.routingproject.R
import com.example.routingproject.adapter.ScheduleAdapter
import com.example.routingproject.adapter.TripForExplorePageAdapter
import com.example.routingproject.data.api.RetrofitClient
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.data.model.TripGetAllResponse
import com.example.routingproject.databinding.FragmentExploreBinding
import com.example.routingproject.databinding.FragmentProfileBinding
import com.example.routingproject.ui.profile.ProfileViewModel
import com.example.routingproject.ui.profile.ProfileViewModelFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding
    private lateinit var exploreViewModel: ExploreViewModel
    private lateinit var tripGetAllResponse: TripGetAllResponse
    private lateinit var tripForExplorePageAdapter: TripForExplorePageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater, container, false)

        val userRepository = UserRepository(RetrofitClient.apiService,requireContext())

        exploreViewModel = ViewModelProvider(
            this,
            ExploreViewModelFactory(userRepository)
        )[ExploreViewModel::class.java]

        lifecycleScope.launch {
            async {
                tripGetAllResponse = exploreViewModel.getTrips()!!
                val recyclerView = binding.recyclerView
                tripForExplorePageAdapter = TripForExplorePageAdapter(tripGetAllResponse.data)
                recyclerView.adapter = tripForExplorePageAdapter
                recyclerView.layoutManager = LinearLayoutManager(activity)
            }
        }



        return binding.root
    }

}

class ExploreViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExploreViewModel::class.java)) {
            return ExploreViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}