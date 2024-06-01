package com.example.routingproject.data.model

data class TripGetAllResponse(
    val data: List<TripGetAllData>,
    val errors: Any,
    val message: Any,
    val pageNumber: Int,
    val pageSize: Int,
    val succeeded: Boolean
)

data class TripGetAllData(
    val isFavourite: Boolean,
    val stops: List<Stop>,
    val tripDescription: String,
    val tripId: Int,
    val totalTripTime: Int,
    val tripName: String,
    val tripStartTime: String,
    val userId: String
)