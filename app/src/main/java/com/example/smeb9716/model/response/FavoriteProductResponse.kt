package com.example.smeb9716.model.response

import com.example.smeb9716.foundation.BaseResponse
import com.google.gson.annotations.SerializedName

data class FavoriteProductResponse(
    val data: List<FavoriteProductData>? = emptyList(),
): BaseResponse()

data class FavoriteProductData(
    @SerializedName("_id")
    val id: String,
    @SerializedName("productId")
    val product: FavoriteProduct
)

data class FavoriteProduct(
    @SerializedName("_id")
    val id: String,
    val name: String? = "",
)