package com.example.smeb9716.model.response

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.model.Cart

data class GetCartsResponse(
    val data: List<Cart>
) : BaseResponse()

data class GetCartDetailResponse(
    val data: Cart
) : BaseResponse()
