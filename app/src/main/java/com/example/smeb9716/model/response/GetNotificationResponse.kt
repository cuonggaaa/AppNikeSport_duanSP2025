package com.example.smeb9716.model.response

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.model.Notification

data class GetNotificationResponse(
    val data: List<Notification>? = emptyList(),
): BaseResponse()
