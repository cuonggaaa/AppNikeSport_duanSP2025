package com.example.smeb9716.model

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("_id") val id: String,
    val userId: User? = null,
    val voucherId: Voucher? = null,
    val paymentMethodId: PaymentMethod? = null,
    val cartData: List<CartOrder>? = null,
    val cartId: List<String>? = null,
    val totalAmount: Long? = 0L,
    val status: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val address: String? = null,
)
