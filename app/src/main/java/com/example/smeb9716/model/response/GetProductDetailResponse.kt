package com.example.smeb9716.model.response

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.model.Product

data class GetProductDetailResponse(
    val data: Product? = null,
): BaseResponse()
