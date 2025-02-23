package com.example.smeb9716.model.request

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val phone: String,
    val address: String
)
