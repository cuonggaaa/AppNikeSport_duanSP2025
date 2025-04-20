package com.example.smeb9716.model.request

data class AddCartRequest(
    val productId: String, val quantity: Int, val userId: String, val size: String
)