package com.example.routingproject.data.model

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val userName: String,
    val phoneNumber: String,
    val tcNo: Long,
    val birthofYear: Int,
    val password: String,
    val confirmPassword: String
)

