package com.example.smeb9716.model

import com.google.gson.annotations.SerializedName

data class Banner(
    @SerializedName("_id")
    val id: String,
    val name: String? = "",
    val image: String,
    val description: String? = ""
)
