package com.example.routingproject.data.model

data class ChangePasswordRequest(
    val confirmNewPassword: String,
    val newPassword: String,
    val oldPassword: String
)