package com.example.smeb9716.foundation

open class BaseResponse(
    val success: Boolean = true,
    val message: String? = null,
    val error: String? = null,
)
