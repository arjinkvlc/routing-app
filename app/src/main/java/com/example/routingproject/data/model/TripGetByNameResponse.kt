package com.example.routingproject.data.model

data class TripGetByNameResponse(
    val `data`: List<TripGetByNameData>,
    val errors: Any,
    val message: Any,
    val pageNumber: Int,
    val pageSize: Int,
    val succeeded: Boolean
)

data class TripGetByNameData(
    val isFavourite: Boolean,
    val stops: List<Any>,
    val tripDescription: String,
    val tripId: Int,
    val tripName: String,
    val tripStartTime: String,
    val user: Any,
    val userId: String
)