package com.example.smeb9716.model.response

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.model.User

data class GetUserResponse(
    val user: User
): BaseResponse()
