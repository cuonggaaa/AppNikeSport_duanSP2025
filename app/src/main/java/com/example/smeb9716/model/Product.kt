package com.example.smeb9716.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("_id") val id: String,
    val name: String? = "",
    val price: Long? = 0,
    val image: List<String>? = emptyList(),
    val description: String? = "",
    @SerializedName("categorysId") val category: Category? = null,
    val discount: Long? = 0,
    val quantity: Int? = 0,
    val favoritesValue: Int? = 0,
    val reviewValue: Double? = 0.0,
    val updatedAt: String? = "",
    val reviewCount: Int? = 0,
    var isFavorite: Boolean? = false,
) {
    fun getDiscountedPrice(): Long {
        return (price?.minus(discount ?: 0) ?: 0).coerceAtLeast(0)
    }
}
