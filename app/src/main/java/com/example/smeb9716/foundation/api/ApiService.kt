package com.example.smeb9716.foundation.api

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.model.User
import com.example.smeb9716.model.request.LoginRequest
import com.example.smeb9716.model.request.RegisterRequest
import com.example.smeb9716.model.response.BannerResponse
import com.example.smeb9716.model.response.CategoryResponse
import com.example.smeb9716.model.response.GetAllProductResponse
import com.example.smeb9716.model.response.GetUserResponse
import com.example.smeb9716.model.response.GetVoucherResponse
import com.example.smeb9716.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("posts")
    suspend fun getUsers(): Response<List<User>>

    @POST("api/user/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/user/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<BaseResponse>

    @GET("api/user/{uid}")
    suspend fun getUser(@Path("uid") uid: String): Response<GetUserResponse>

    @GET("api/banner/get-all")
    suspend fun getBanners(
        @Query("page") page: Int = 1, @Query("limit") limit: Int = 10,
    ): Response<BannerResponse>

    @GET("api/category/get-all")
    suspend fun getCategories(
        @Query("page") page: Int = 1, @Query("limit") limit: Int = 10,
        ): Response<CategoryResponse>

    @GET("api/voucher/get-all")
    suspend fun getVouchers(): Response<GetVoucherResponse>
}
