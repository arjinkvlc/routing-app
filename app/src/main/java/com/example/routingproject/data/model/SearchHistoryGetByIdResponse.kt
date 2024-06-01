package com.example.routingproject.data.model

data class SearchHistoryGetByIdResponse(
    val `data`: SearchHistoryGetByIdData,
    val errors: Any,
    val message: Any,
    val succeeded: Boolean
)

data class SearchHistoryGetByIdData(
    val searchId: Int,
    val searchString: String,
    val searchTime: String,
    val user: Any,
    val userId: String
)