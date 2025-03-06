package com.example.smeb9716.model.response

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.model.Voucher

data class GetVoucherResponse(
    val data: List<Voucher>? = emptyList(),
): BaseResponse()
