package com.example.routingproject.data.model

data class AddLocationRequest(
    val latitude: Double,
    val locationAdress: String,
    val locationName: String,
    val longitude: Double
)