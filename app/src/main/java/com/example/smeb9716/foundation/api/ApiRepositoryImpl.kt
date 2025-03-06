package com.example.smeb9716.foundation.api

import com.example.smeb9716.foundation.BaseRepository
import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.foundation.Data
import com.example.smeb9716.model.WholeApp
import com.example.smeb9716.model.request.LoginRequest
import com.example.smeb9716.model.request.RegisterRequest
import com.example.smeb9716.model.response.BannerResponse
import com.example.smeb9716.model.response.CategoryResponse
import com.example.smeb9716.model.response.GetAllProductResponse
import com.example.smeb9716.model.response.GetUserResponse
import com.example.smeb9716.model.response.GetVoucherResponse
import com.example.smeb9716.model.response.LoginResponse
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(private val apiService: ApiService) : ApiRepository,
    BaseRepository() {
    override suspend fun login(loginRequest: LoginRequest): Data<LoginResponse> = safeCallApi {
        apiService.loginUser(loginRequest)
    }

    override suspend fun register(registerRequest: RegisterRequest): Data<BaseResponse> {
        return safeCallApi {
            apiService.registerUser(registerRequest)
        }
    }

    override suspend fun getUserDetail(uid: String): Data<GetUserResponse> {
        return safeCallApi {
            apiService.getUser(WholeApp.USER_ID)
        }
    }

    override suspend fun getBanners(page: Int, limit: Int): Data<BannerResponse> {
        return safeCallApi {
            apiService.getBanners(page, limit)
        }
    }

    override suspend fun getCategories(page: Int, limit: Int): Data<CategoryResponse> {
        return safeCallApi {
            apiService.getCategories(page, limit)
        }
    }
    override suspend fun getVouchers(): Data<GetVoucherResponse> {
        return safeCallApi {
            apiService.getVouchers()
        }
    }
}
