package com.example.smeb9716.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("_id")
    val id: String,
    val name: String? = "",
    val image: String? = "",
)
