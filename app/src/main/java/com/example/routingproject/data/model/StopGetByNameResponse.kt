package com.example.routingproject.data.model

data class StopGetByNameResponse(
    val `data`: List<StopGetByNameData>,
    val errors: Any,
    val message: Any,
    val pageNumber: Int,
    val pageSize: Int,
    val succeeded: Boolean
)

data class StopGetByNameData(
    val location: Any,
    val locationId: Int,
    val priority: Int,
    val stopArriveTime: String,
    val stopDescription: String,
    val stopId: Int,
    val stopName: String,
    val trip: Any,
    val tripId: Int
)