package com.example.routingproject.data.model

data class ConfirmationResponse(
    val succeeded: Boolean,
    val message: String?,
    val errors: String?,
    val data: String?
)
