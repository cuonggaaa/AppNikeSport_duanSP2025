package com.example.smeb9716.model.request

data class UpdateProfileRequest(
    val address: String? = "",
    val email: String? = "",
    val name: String? = "",
    val phone: String? = "",
)
