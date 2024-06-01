package com.example.routingproject.data.model

data class StopGetAllResponse(
    val `data`: List<StopData>,
    val errors: Any,
    val message: Any,
    val pageNumber: Int,
    val pageSize: Int,
    val succeeded: Boolean
)

data class StopData(
    val locationId: Int,
    val priority: Int,
    val stopArriveTime: String,
    val stopDescription: String,
    val stopId: Int,
    val stopName: String,
    val tripId: Int
)