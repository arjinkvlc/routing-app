package com.example.routingproject.data.model

data class LoginResponse(
    val `data`: Data,
    val errors: Any,
    val message: String,
    val succeeded: Boolean
)