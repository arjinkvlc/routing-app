package com.example.routingproject.data.model

data class LocationGetByNameResponse(
    val `data`: List<LocationGetByNameData>,
    val errors: Any,
    val message: Any,
    val pageNumber: Int,
    val pageSize: Int,
    val succeeded: Boolean
)

data class LocationGetByNameData(
    val address: Any,
    val latitude: Int,
    val locationId: Int,
    val locationName: String,
    val longitude: Int
)