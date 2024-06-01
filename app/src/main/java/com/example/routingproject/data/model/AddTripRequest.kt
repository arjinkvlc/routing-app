package com.example.routingproject.data.model

data class AddTripRequest(
    val isFavourite: Boolean,
    val tripDescription: String,
    val tripName: String,
    val tripStartTime: String
)