package com.example.smeb9716.model.response

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.model.PaymentMethod

data class PaymentMethodResponse(
    val data: List<PaymentMethod>
) : BaseResponse()
