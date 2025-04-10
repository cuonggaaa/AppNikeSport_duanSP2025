package com.example.smeb9716.model.request

data class OrderRequest(
    val userId: String,
    val cartId: List<String>,
    val paymentMethodId: String = "673aed15ad0cf16b1480e2eb",
    val voucherId: String? = null,
    val language: String = "vi",
    val address: String? = null,
)
