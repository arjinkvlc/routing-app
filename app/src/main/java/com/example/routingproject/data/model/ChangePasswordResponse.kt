package com.example.routingproject.data.model

data class ChangePasswordResponse(
    val `data`: String,
    val errors: Any,
    val message: String,
    val succeeded: Boolean
)