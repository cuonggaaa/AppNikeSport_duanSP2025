package com.example.smeb9716.model.response

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.model.Product

data class GetAllProductResponse(
    val data: GetAllProductData,
): BaseResponse()

data class GetAllProductData(
    val data: List<Product>,
)