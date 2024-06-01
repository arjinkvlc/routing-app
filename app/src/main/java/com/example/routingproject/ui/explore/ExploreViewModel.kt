package com.example.routingproject.ui.explore

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.data.model.TripGetAllResponse

class ExploreViewModel(private val userRepository: UserRepository):
    ViewModel() {
    suspend fun getTrips(): TripGetAllResponse? {
        val tripResponse =userRepository.getTrips(1,50)
        if (tripResponse.succeeded) {
            return tripResponse
        } else {
            Log.d("ProfileViewModel", "Password changing failed: ${tripResponse.message}")
            return null
        }

    }
}