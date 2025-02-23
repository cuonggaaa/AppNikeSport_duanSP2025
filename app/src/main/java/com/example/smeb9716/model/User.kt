package com.example.smeb9716.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val id: String,
    val name: String? = "",
    val email: String? = "",
    val phone: String? = "",
    val address: String? = "",
    val createdAt: String? = "",
    val updatedAt: String? = "",
    @SerializedName("is_admin")
    val isAdmin: Boolean? = false
)
