package com.example.smeb9716.model.response

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.model.Category

data class CategoryResponse(
    val data: CategoryData
): BaseResponse()

data class CategoryData(
    val data: List<Category>
)
