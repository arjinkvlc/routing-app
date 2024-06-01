package com.example.routingproject.data.model

data class ChangeTripRequest(
    val latitude: Int,
    val locationAdress: String,
    val locationId: Int,
    val locationName: String,
    val longitude: Int
)