package com.example.routingproject.data.model

data class StopRequest(
    val arriveTime: String,
    val locationId: Int,
    val priority: Int,
    val stopDescription: String,
    val stopName: String,
    val tripId: Int,
    val spendingTimeAsMinute: Int
)