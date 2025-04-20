package com.example.smeb9716.model

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("_id")
    val id: String,
    val userId: String? = "",
    val content: String? = "",
    val status: Int? = 0,
    val createdAt: String? = "",
    val updatedAt: String? = "",
)
