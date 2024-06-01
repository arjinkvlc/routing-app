package com.example.routingproject.ui.favorite

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.routingproject.R
import com.example.routingproject.adapter.FavoritesAdapter
import com.example.routingproject.adapter.ScheduleAdapter
import com.example.routingproject.data.api.RetrofitClient
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.data.model.AddTripRequest
import com.example.routingproject.data.model.TripGetAllData
import com.example.routingproject.databinding.FragmentFavoritesBinding
import com.example.routingproject.databinding.FragmentScheduleBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var favoritesViewModel: FavoritesViewModel
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var tripGetAllData: List<TripGetAllData>
    private lateinit var recyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentFavoritesBinding.inflate(inflater,container,false)

        val userRepository = UserRepository(RetrofitClient.apiService,requireContext())

        favoritesViewModel = ViewModelProvider(
            this,
            FavoritesViewModelFactory(userRepository,requireContext())
        )[FavoritesViewModel::class.java]

        lifecycleScope.launch {
            async {
                tripGetAllData = favoritesViewModel.getTrips(requireContext())
                recyclerView = binding.recyclerviewList
                favoritesAdapter = FavoritesAdapter(tripGetAllData)
                recyclerView.adapter=favoritesAdapter
                recyclerView.layoutManager=LinearLayoutManager(activity)
            }
        }

        return binding.root
    }

}
class FavoritesViewModelFactory(private val userRepository: UserRepository,private val context:Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(userRepository,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
