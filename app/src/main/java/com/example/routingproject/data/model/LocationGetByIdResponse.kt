package com.example.routingproject.data.model

data class LocationGetByIdResponse(
    val `data`: LocationGetByIdData,
    val errors: Any,
    val message: Any,
    val succeeded: Boolean
)

data class LocationGetByIdData(
    val address: String,
    val latitude: Double,
    val locationId: Int,
    val locationName: String,
    val longitude: Double
)