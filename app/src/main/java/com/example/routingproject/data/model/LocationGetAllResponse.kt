package com.example.routingproject.data.model

data class LocationGetAllResponse(
    val `data`: List<LocationGetAllData>,
    val errors: Any,
    val message: Any,
    val pageNumber: Int,
    val pageSize: Int,
    val succeeded: Boolean
)

data class LocationGetAllData(
    val latitude: Double,
    val locationId: Int,
    val locationName: String,
    val longtitude: Double
)