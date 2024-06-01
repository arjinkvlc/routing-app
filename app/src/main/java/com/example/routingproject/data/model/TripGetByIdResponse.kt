package com.example.routingproject.data.model

data class TripGetByIdResponse(
    val `data`: TripGetByIdData,
    val errors: Any,
    val message: Any,
    val succeeded: Boolean
)

data class TripGetByIdData(
    val isFavourite: Boolean,
    val stops: List<Stop>,
    val tripDescription: String,
    val tripId: Int,
    val tripName: String,
    val tripStartTime: String,
    val user: Any,
    val userId: String
)