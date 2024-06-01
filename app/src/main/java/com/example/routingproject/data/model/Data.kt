package com.example.routingproject.data.model

data class Data(
    val email: String,
    val id: String,
    val isVerified: Boolean,
    val jwToken: String,
    val refreshToken: String,
    val roles: List<String>,
    val userName: String
)