package com.example.smeb9716.model.response

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.model.Order

data class GetOrderResponse(
    val data: List<Order>,
): BaseResponse()
