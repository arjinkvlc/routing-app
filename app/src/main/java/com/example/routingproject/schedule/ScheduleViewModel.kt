package com.example.routingproject.schedule

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routingproject.SharedPreferencesHelper
import com.example.routingproject.data.api.RetrofitClient.apiService
import com.example.routingproject.data.api.UserRepository
import com.example.routingproject.data.model.AddTripRequest
import com.example.routingproject.data.model.TripGetAllData
import com.example.routingproject.data.model.TripGetAllResponse
import kotlinx.coroutines.launch


class ScheduleViewModel(private val userRepository: UserRepository,context:Context) : ViewModel() {
    var succeeded: Boolean = false
    var data: String? = ""
    private val sharedPreferencesHelper = SharedPreferencesHelper(context)


    suspend fun getTrips(activity: Context): List<TripGetAllData> {
        val list= mutableListOf<TripGetAllData>()
        val tripGetAllResponse = userRepository.getTrips(1,100)
        val tripDatas = tripGetAllResponse.data
        val currentUserId = sharedPreferencesHelper.getUserId()
        Log.d("scheduleviewmodel", tripDatas.toString())
        Log.d("scheduleviewmodel",currentUserId.toString())
        for (tripData in tripDatas){
            if(tripData.userId == currentUserId){
                list.add(tripData)
                Log.d("ScheduleViewModel", "Bu trip, mevcut kullanıcının userId'siyle eşleşiyor: $tripData")
            }else {
                Log.d("ScheduleViewModel", "Bu trip, mevcut kullanıcının userId'siyle eşleşmiyor: $tripData")
            }
        }
        return list

    }
    suspend fun addTrip(addTripRequestInput: AddTripRequest): Int {
        val addTripRequest=addTripRequestInput
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        Log.d("testscheduleapi",addTripRequest.toString())
        return apiService.addTrip(addTripRequest, "Bearer $token")
    }
}