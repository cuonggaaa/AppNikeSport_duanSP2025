package com.example.smeb9716.foundation.api

import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.model.User
import com.example.smeb9716.model.request.AddCartRequest
import com.example.smeb9716.model.request.AddFavoriteProductRequest
import com.example.smeb9716.model.request.ChangePasswordRequest
import com.example.smeb9716.model.request.DeleteFavoriteProductRequest
import com.example.smeb9716.model.request.LoginRequest
import com.example.smeb9716.model.request.OrderRequest
import com.example.smeb9716.model.request.RegisterRequest
import com.example.smeb9716.model.request.SendProductReviewRequest
import com.example.smeb9716.model.request.UpdateCartRequest
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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @PUT("api/user/{uid}")
    suspend fun updateUser(
        @Path("uid") uid: String,
        @Body request: UpdateProfileRequest,
    ): Response<BaseResponse>

    @POST("api/user/change-password/{uid}")
    suspend fun changePassword(
        @Path("uid") uid: String, @Body request: ChangePasswordRequest,
    ): Response<BaseResponse>

    @GET("api/banner/get-all")
    suspend fun getBanners(
        @Query("page") page: Int = 1, @Query("limit") limit: Int = 10,
    ): Response<BannerResponse>

    @GET("api/category/get-all")
    suspend fun getCategories(
        @Query("page") page: Int = 1, @Query("limit") limit: Int = 10,

        ): Response<CategoryResponse>

    @GET("api/product/get-all")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("name") name: String? = null,
        @Query("priceMin") priceMin: Long? = null,
        @Query("priceMax") priceMax: Long? = null,
        @Query("sort") sort: String? = null,
        @Query("order") order: String? = null,
    ): Response<GetAllProductResponse>

    @GET("api/product/{pid}")
    suspend fun getProduct(@Path("pid") pid: String): Response<GetProductDetailResponse>

    @GET("api/product-fav/u/{uid}")
    suspend fun getFavoriteProducts(@Path("uid") uid: String): Response<FavoriteProductResponse>

    @POST("api/product-fav")
    suspend fun addFavoriteProduct(@Body request: AddFavoriteProductRequest): Response<AddFavoriteProductResponse>

    @HTTP(method = "DELETE", path = "api/product-fav", hasBody = true)
    suspend fun removeFavoriteProduct(@Body request: DeleteFavoriteProductRequest): Response<BaseResponse>

    @POST("api/product-review")
    suspend fun addProductReview(@Body request: SendProductReviewRequest): Response<BaseResponse>

    @GET("api/voucher/get-all")
    suspend fun getVouchers(): Response<GetVoucherResponse>

    @GET("api/product-review/p/{pid}")
    suspend fun getProductReview(@Path("pid") pid: String): Response<ProductReviewResponse>

    @GET("api/cart/u/{uid}")
    suspend fun getCarts(@Path("uid") uid: String): Response<GetCartsResponse>

    @GET("api/cart/{cartData}")
    suspend fun getCartDetail(@Path("cartData") cartId: String): Response<GetCartDetailResponse>

    @POST("api/cart")
    suspend fun addCart(@Body request: AddCartRequest): Response<BaseResponse>

    @PUT("api/cart/{cartData}")
    suspend fun updateCart(
        @Path("cartData") cartId: String, @Body request: UpdateCartRequest,
    ): Response<BaseResponse>

    @DELETE("api/cart/{cartData}")
    suspend fun deleteCart(@Path("cartData") cartId: String): Response<BaseResponse>

    @GET("api/pay-method/get-all")
    suspend fun getPaymentMethods(): Response<PaymentMethodResponse>

    @POST("api/order")
    suspend fun createOrder(@Body request: OrderRequest): Response<CreateOrderResponse>

    @GET("api/order/u/{uid}")
    suspend fun getOrders(@Path("uid") uid: String): Response<GetOrderResponse>

    @PUT("api/order/cancelled/{orderId}")
    suspend fun cancelOrder(@Path("orderId") orderId: String): Response<BaseResponse>
}
