package com.example.smeb9716.model.response

import com.example.smeb9716.foundation.BaseResponse
import com.google.gson.annotations.SerializedName

data class AddFavoriteProductResponse(
    val data: AddFavoriteProductData,
): BaseResponse()

data class AddFavoriteProductData(
    @SerializedName("_id")
    val id: String,
    val productId: String
)