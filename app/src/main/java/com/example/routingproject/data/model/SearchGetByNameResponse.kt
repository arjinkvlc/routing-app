package com.example.routingproject.data.model

data class SearchGetByNameResponse(
    val `data`: List<SearchGetByNameData>,
    val errors: Any,
    val message: Any,
    val pageNumber: Int,
    val pageSize: Int,
    val succeeded: Boolean
)

data class SearchGetByNameData(
    val searchId: Int,
    val searchString: String,
    val searchTime: String,
    val user: Any,
    val userId: String
)