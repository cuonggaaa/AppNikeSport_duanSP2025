package com.example.smeb9716.model.request

data class SendProductReviewRequest(
    val userId: String,
    val productId: String,
    val rating: Double,
    val reviewText: String
)
