package com.example.smeb9716.model.response

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.model.Banner

data class BannerResponse(
    val data: BannerData
): BaseResponse()

data class BannerData(
    val data: List<Banner>
)