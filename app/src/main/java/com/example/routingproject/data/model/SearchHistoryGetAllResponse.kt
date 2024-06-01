package com.example.routingproject.data.model

data class SearchHistoryGetAllResponse(
    val `data`: List<SearchHistoryData>,
    val errors: Any,
    val message: Any,
    val pageNumber: Int,
    val pageSize: Int,
    val succeeded: Boolean
)