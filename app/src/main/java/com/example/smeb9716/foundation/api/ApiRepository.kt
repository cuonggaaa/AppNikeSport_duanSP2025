package com.example.smeb9716.foundation.api

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.foundation.Data
import com.example.smeb9716.model.request.LoginRequest
import com.example.smeb9716.model.request.RegisterRequest
import com.example.smeb9716.model.response.GetUserResponse
import com.example.smeb9716.model.response.LoginResponse

interface ApiRepository {
    suspend fun login(loginRequest: LoginRequest): Data<LoginResponse>
    suspend fun register(registerRequest: RegisterRequest): Data<BaseResponse>
    suspend fun getUserDetail(uid: String): Data<GetUserResponse>
}
