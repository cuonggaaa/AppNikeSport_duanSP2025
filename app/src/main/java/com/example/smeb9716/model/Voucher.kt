package com.example.smeb9716.model

import com.google.gson.annotations.SerializedName

data class Voucher(
    @SerializedName("_id") val id: String,
    val code: String,
    val description: String? = "",
    val startDate: String? = "",
    val endDate: String? = "",
    val discountValue: Long? = 0,
    val status: String? = "Active",
    val usageLimit: Int? = 0,
    val image: String? = "",
    var selected: Boolean? = false
)
