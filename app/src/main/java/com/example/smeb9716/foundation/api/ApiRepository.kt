package com.example.smeb9716.foundation.api

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.foundation.Data
import com.example.smeb9716.model.Cart
import com.example.smeb9716.model.PaymentMethod
import com.example.smeb9716.model.Voucher
import com.example.smeb9716.model.filter.ProductSortBy
import com.example.smeb9716.model.filter.ProductSortMode
import com.example.smeb9716.model.request.LoginRequest
import com.example.smeb9716.model.request.RegisterRequest
import com.example.smeb9716.model.request.UpdateProfileRequest
import com.example.smeb9716.model.response.AddFavoriteProductResponse
import com.example.smeb9716.model.response.BannerResponse
import com.example.smeb9716.model.response.CategoryResponse
import com.example.smeb9716.model.response.CreateOrderResponse
import com.example.smeb9716.model.response.FavoriteProductResponse
import com.example.smeb9716.model.response.GetAllProductResponse
import com.example.smeb9716.model.response.GetCartDetailResponse
import com.example.smeb9716.model.response.GetCartsResponse
import com.example.smeb9716.model.response.GetOrderResponse
import com.example.smeb9716.model.response.GetProductDetailResponse
import com.example.smeb9716.model.response.GetUserResponse
import com.example.smeb9716.model.response.GetVoucherResponse
import com.example.smeb9716.model.response.LoginResponse
import com.example.smeb9716.model.response.PaymentMethodResponse
import com.example.smeb9716.model.response.ProductReviewResponse

interface ApiRepository {
    suspend fun login(loginRequest: LoginRequest): Data<LoginResponse>
    suspend fun register(registerRequest: RegisterRequest): Data<BaseResponse>
    suspend fun getUserDetail(uid: String): Data<GetUserResponse>
    suspend fun updateUser(
        uid: String, request: UpdateProfileRequest,
    ): Data<BaseResponse>

    suspend fun changePassword(
        uid: String, oldPassword: String, newPassword: String,
    ): Data<BaseResponse>

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

    suspend fun reviewProduct(
        productId: String, userId: String, rating: Double, content: String,
    ): Data<BaseResponse>

    suspend fun getVouchers(): Data<GetVoucherResponse>
    suspend fun getProductReviews(productId: String): Data<ProductReviewResponse>
    suspend fun getCarts(userId: String): Data<GetCartsResponse>
    suspend fun addCart(
        productId: String, quantity: Int, userId: String,
        size: String,
    ): Data<BaseResponse>

    suspend fun updateCart(
        cartId: String,
        quantity: Int,
    ): Data<BaseResponse>

    suspend fun deleteCart(cartId: String): Data<BaseResponse>
    suspend fun getCartDetail(cartId: String): Data<GetCartDetailResponse>
    suspend fun getPaymentMethods(): Data<PaymentMethodResponse>
    suspend fun createOrder(
        userId: String,
        carts: List<Cart>,
        paymentMethod: PaymentMethod,
        voucher: Voucher? = null,
        address: String? = null,
    ): Data<CreateOrderResponse>

    suspend fun getOrders(userId: String): Data<GetOrderResponse>
    suspend fun cancelOrder(orderId: String): Data<BaseResponse>
}
