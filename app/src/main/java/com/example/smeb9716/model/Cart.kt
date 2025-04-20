package com.example.smeb9716.model

import com.google.gson.annotations.SerializedName

data class Cart(
    @SerializedName("_id") val id: String,
    val userId: String,
    val productId: Product,
    var quantity: Int,
    val createdAt: String? = "",
    val updatedAt: String? = "",
    var selected: Boolean? = false
) {
    fun toCartOrder(): CartOrder {
        return CartOrder(
            id = id,
            userId = CartUserId(id = userId, name = ""),
            productId = productId,
            quantity = quantity,
            createdAt = createdAt,
            updatedAt = updatedAt,
            selected = selected
        )
    }
}

data class CartOrder(
    @SerializedName("_id") val id: String,
    val userId: CartUserId,
    val productId: Product,
    var quantity: Int,
    var size: String = "",
    val createdAt: String? = "",
    val updatedAt: String? = "",
    var selected: Boolean? = false
)

data class CartUserId(
    @SerializedName("_id")
    val id: String,
    val name: String? = "",
)