package com.example.routingproject.data.api

import android.content.Context
import android.util.Log
import com.example.routingproject.SharedPreferencesHelper
import com.example.routingproject.data.model.*

class UserRepository(private val apiService: ApiService, context: Context) {

    private val sharedPreferencesHelper = SharedPreferencesHelper(context)

    // Account operations
    suspend fun register(registerRequest: RegisterRequest): RegisterResponse {
        return apiService.register(registerRequest)
    }

    suspend fun confirmEmail(confirmationRequest: ConfirmationRequest): ConfirmationResponse {
        return apiService.confirmEmail(confirmationRequest)
    }

    suspend fun authenticate(loginRequest: LoginRequest): LoginResponse {
        return apiService.authenticate(loginRequest)
    }

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): ChangePasswordResponse {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.changePassword(changePasswordRequest, "Bearer $token")
    }

    // Location operations
    suspend fun getLocations(pageNumber: Int, pageSize: Int): LocationGetAllResponse {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.getLocations(pageNumber, pageSize, "Bearer $token")
    }

    suspend fun addLocation(addLocationRequest: AddLocationRequest): Int {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.addLocation(addLocationRequest, "Bearer $token")
    }

    suspend fun getLocationById(id: Int): LocationGetByIdResponse {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.getLocationById(id, "Bearer $token")
    }

    suspend fun changeLocationById(id: Int, changeLocationRequest: ChangeLocationRequest): Int {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.changeLocationById(changeLocationRequest, id, "Bearer $token")
    }

    suspend fun deleteLocationById(id: Int) {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        apiService.deleteLocationById(id, "Bearer $token")
    }

    suspend fun getLocationByName(locationName: String, pageNumber: Int, pageSize: Int): LocationGetByNameResponse {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.getLocationByName(locationName, pageNumber, pageSize, "Bearer $token")
    }

    // Search History operations
    suspend fun getSearchHistory(pageNumber: Int, pageSize: Int): SearchHistoryGetAllResponse {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.getSearchHistory(pageNumber, pageSize, "Bearer $token")
    }

    suspend fun postSearch(searchString: String): Int {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        Log.d("testsearch",token+searchString)
        return apiService.postSearch("1", searchString,"Bearer $token")
    }

    suspend fun getSearchHistoryById(id: Int): SearchHistoryGetByIdResponse {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.getSearchHistoryById(id, "Bearer $token")
    }

    suspend fun deleteSearchById(id: Int) {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        apiService.deleteSearchById(id, "Bearer $token")
    }

    suspend fun getSearchHistoryByName(searchString: String, pageNumber: Int, pageSize: Int): SearchGetByNameResponse {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.getSearchHistoryByName(searchString, pageNumber, pageSize, "Bearer $token")
    }

    // Stop operations
    suspend fun getAllStops(pageNumber: Int, pageSize: Int): StopGetAllResponse {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.getAllStops(pageNumber, pageSize, "Bearer $token")
    }

    suspend fun postStop(stopRequest: StopRequest): Int {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.postStop(stopRequest, "Bearer $token")
    }

    suspend fun getStopExistsById(id: Int): StopGetByIdResponse {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.getStopExistsById(id, "Bearer $token")
    }

    suspend fun deleteStopById(id: Int) {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        apiService.deleteStopById(id, "Bearer $token")
    }

    suspend fun getStopByName(stopName: String, pageNumber: Int, pageSize: Int): StopGetByNameResponse {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.getStopByName(stopName, pageNumber, pageSize, "Bearer $token")
    }

    // Trip operations
    suspend fun getTrips(pageNumber: Int, pageSize: Int): TripGetAllResponse {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.getTrips(pageNumber, pageSize, "Bearer $token")
    }

    suspend fun addTrip(addTripRequest: AddTripRequest): Int {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.addTrip(addTripRequest, "Bearer $token")
    }

    suspend fun getTripById(id: Int): TripGetByIdResponse {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.getTripById(id, "Bearer $token")
    }

    suspend fun changeTripById(id: Int, changeTripRequest: ChangeTripRequest): Int {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.changeTripById(changeTripRequest, id, "Bearer $token")
    }

    suspend fun deleteTripById(id: Int) : DeleteTripByIdResponse{
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.deleteTripById(id, "Bearer $token")
    }

    suspend fun getTripByName(tripName: String, pageNumber: Int, pageSize: Int): TripGetByNameResponse {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.getTripByName(tripName, pageNumber, pageSize, "Bearer $token")
    }

    suspend fun changeFavoriteFlagForTrip(tripId: Int, isFavorite: Boolean): Boolean {
        val token = sharedPreferencesHelper.getJwtToken() ?: ""
        return apiService.changeFavoriteFlagForTrip(tripId, isFavorite, "Bearer $token")
    }
}
