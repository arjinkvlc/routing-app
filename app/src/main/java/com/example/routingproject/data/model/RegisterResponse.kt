package com.example.routingproject.data.model

data class RegisterResponse(
    val succeeded: Boolean,
    val message: String?,
    val errors: String?,
    val data: String?
)

