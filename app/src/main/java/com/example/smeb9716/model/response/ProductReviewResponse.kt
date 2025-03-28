package com.example.smeb9716.model.response

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.model.User

data class ProductReviewResponse(
    val data: List<ProductReview>? = emptyList()
) : BaseResponse()

data class ProductReview(
    val userId: User?, val rating: Int? = 0, val reviewText: String? = ""
)
