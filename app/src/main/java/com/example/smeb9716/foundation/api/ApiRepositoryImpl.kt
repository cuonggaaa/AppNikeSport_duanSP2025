package com.example.smeb9716.foundation.api

import com.example.smeb9716.foundation.BaseRepository
import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.foundation.Data
import com.example.smeb9716.model.WholeApp
import com.example.smeb9716.model.request.LoginRequest
import com.example.smeb9716.model.response.GetUserResponse
import com.example.smeb9716.model.response.LoginResponse
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(private val apiService: ApiService) : ApiRepository,
    BaseRepository() {
    override suspend fun login(loginRequest: LoginRequest): Data<LoginResponse> = safeCallApi {
        apiService.loginUser(loginRequest)
    }

    override suspend fun getUserDetail(uid: String): Data<GetUserResponse> {
        return safeCallApi {
            apiService.getUser(WholeApp.USER_ID)
        }
    }
}
