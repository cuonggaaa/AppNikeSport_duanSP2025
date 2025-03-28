package com.example.smeb9716.foundation.api

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.foundation.Data
import com.example.smeb9716.model.filter.ProductSortBy
import com.example.smeb9716.model.filter.ProductSortMode
import com.example.smeb9716.model.request.LoginRequest
import com.example.smeb9716.model.request.RegisterRequest
import com.example.smeb9716.model.response.AddFavoriteProductResponse
import com.example.smeb9716.model.response.BannerResponse
import com.example.smeb9716.model.response.CategoryResponse
import com.example.smeb9716.model.response.FavoriteProductResponse
import com.example.smeb9716.model.response.GetAllProductResponse
import com.example.smeb9716.model.response.GetProductDetailResponse
import com.example.smeb9716.model.response.GetUserResponse
import com.example.smeb9716.model.response.GetVoucherResponse
import com.example.smeb9716.model.response.LoginResponse
import com.example.smeb9716.model.response.ProductReviewResponse

interface ApiRepository {
    suspend fun login(loginRequest: LoginRequest): Data<LoginResponse>
    suspend fun register(registerRequest: RegisterRequest): Data<BaseResponse>
    suspend fun getUserDetail(uid: String): Data<GetUserResponse>
    suspend fun getBanners(page: Int = 1, limit: Int = 10): Data<BannerResponse>
    suspend fun getCategories(page: Int = 1, limit: Int = 10): Data<CategoryResponse>
    suspend fun getProducts(
        page: Int = 1,
        limit: Int = 10,
        name: String? = null,
        priceMin: Long? = null,
        priceMax: Long? = null,
        sort: ProductSortBy? = null,
        order: ProductSortMode? = null,
    ): Data<GetAllProductResponse>
    suspend fun getProductDetail(productId: String): Data<GetProductDetailResponse>
    suspend fun getFavoriteProducts(uid: String): Data<FavoriteProductResponse>
    suspend fun addFavoriteProduct(
        userId: String, productId: String,
    ): Data<AddFavoriteProductResponse>

    suspend fun removeFavoriteProduct(productId: String, userId: String): Data<BaseResponse>
    suspend fun getVouchers(): Data<GetVoucherResponse>
    suspend fun getProductReviews(productId: String): Data<ProductReviewResponse>
}
