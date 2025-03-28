package com.example.smeb9716.model.request

data class ChangePasswordRequest(
    val newPassword: String,
    val oldPassword: String,
)
