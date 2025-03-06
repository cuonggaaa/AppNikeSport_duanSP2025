package com.example.smeb9716.foundation.api

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.foundation.Data
import com.example.smeb9716.model.request.LoginRequest
import com.example.smeb9716.model.request.RegisterRequest
import com.example.smeb9716.model.response.BannerResponse
import com.example.smeb9716.model.response.CategoryResponse
import com.example.smeb9716.model.response.GetAllProductResponse
import com.example.smeb9716.model.response.GetUserResponse
import com.example.smeb9716.model.response.GetVoucherResponse
import com.example.smeb9716.model.response.LoginResponse

interface ApiRepository {
    suspend fun login(loginRequest: LoginRequest): Data<LoginResponse>
    suspend fun register(registerRequest: RegisterRequest): Data<BaseResponse>
    suspend fun getUserDetail(uid: String): Data<GetUserResponse>
    suspend fun getBanners(page: Int = 1, limit: Int = 10): Data<BannerResponse>
    suspend fun getCategories(page: Int = 1, limit: Int = 10): Data<CategoryResponse>
    suspend fun getVouchers(): Data<GetVoucherResponse>
}
