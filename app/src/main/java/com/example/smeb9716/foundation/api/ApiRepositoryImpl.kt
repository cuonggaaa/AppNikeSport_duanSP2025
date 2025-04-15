package com.example.smeb9716.foundation.api

import com.example.smeb9716.foundation.BaseRepository
import com.example.smeb9716.foundation.BaseResponse
import com.example.smeb9716.foundation.Data
import com.example.smeb9716.model.Cart
import com.example.smeb9716.model.PaymentMethod
import com.example.smeb9716.model.Voucher
import com.example.smeb9716.model.WholeApp
import com.example.smeb9716.model.filter.ProductSortBy
import com.example.smeb9716.model.filter.ProductSortMode
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

    override suspend fun updateUser(
        uid: String,
        request: UpdateProfileRequest,
    ): Data<BaseResponse> {
        return safeCallApi {
            apiService.updateUser(uid, request)
        }
    }

    override suspend fun changePassword(
        uid: String,
        oldPassword: String,
        newPassword: String,
    ): Data<BaseResponse> {
        return safeCallApi {
            apiService.changePassword(
                uid,
                ChangePasswordRequest(oldPassword = oldPassword, newPassword = newPassword)
            )
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

    override suspend fun getProducts(
        page: Int,
        limit: Int,
        name: String?,
        priceMin: Long?,
        priceMax: Long?,
        sort: ProductSortBy?,
        order: ProductSortMode?,
    ): Data<GetAllProductResponse> {
        val sortMode = sort?.name?.lowercase()
        val sortBy = order?.name?.lowercase()
        return safeCallApi {
            apiService.getProducts(
                page = page,
                limit = limit,
                name = name,
                priceMin = priceMin,
                priceMax = priceMax,
                sort = sortMode,
                order = sortBy
            )
        }
    }

    override suspend fun getProductDetail(productId: String): Data<GetProductDetailResponse> {
        return safeCallApi {
            apiService.getProduct(productId)
        }
    }

    override suspend fun getFavoriteProducts(uid: String): Data<FavoriteProductResponse> {
        return safeCallApi {
            apiService.getFavoriteProducts(uid)
        }
    }

    override suspend fun addFavoriteProduct(
        userId: String, productId: String,
    ): Data<AddFavoriteProductResponse> {
        return safeCallApi {
            apiService.addFavoriteProduct(
                AddFavoriteProductRequest(
                    userId = userId, productId = productId
                )
            )
        }
    }

    override suspend fun removeFavoriteProduct(
        favoriteId: String, userId: String,
    ): Data<BaseResponse> {
        return safeCallApi {
            apiService.removeFavoriteProduct(
                DeleteFavoriteProductRequest(
                    userId = userId, productId = favoriteId
                )
            )
        }
    }

    override suspend fun reviewProduct(
        productId: String,
        userId: String,
        rating: Double,
        content: String,
    ): Data<BaseResponse> {
        return safeCallApi {
            apiService.addProductReview(
                SendProductReviewRequest(
                    productId = productId, userId = userId, rating = rating, reviewText = content
                )
            )
        }
    }

    override suspend fun getVouchers(): Data<GetVoucherResponse> {
        return safeCallApi {
            apiService.getVouchers()
        }
    }

    override suspend fun getProductReviews(productId: String): Data<ProductReviewResponse> {
        return safeCallApi {
            apiService.getProductReview(productId)
        }
    }

    override suspend fun getCarts(userId: String): Data<GetCartsResponse> {
        return safeCallApi {
            apiService.getCarts(userId)
        }
    }

    override suspend fun addCart(
        productId: String, quantity: Int, userId: String,
    ): Data<BaseResponse> {
        return safeCallApi {
            apiService.addCart(
                AddCartRequest(
                    productId = productId, quantity = quantity, userId = userId
                )
            )
        }
    }

    override suspend fun updateCart(cartId: String, quantity: Int): Data<BaseResponse> {
        return safeCallApi {
            apiService.updateCart(
                cartId = cartId, request = UpdateCartRequest(quantity = quantity)
            )
        }
    }

    override suspend fun deleteCart(cartId: String): Data<BaseResponse> {
        return safeCallApi {
            apiService.deleteCart(cartId)
        }
    }

    override suspend fun getCartDetail(cartId: String): Data<GetCartDetailResponse> {
        return safeCallApi {
            apiService.getCartDetail(cartId)
        }
    }

    override suspend fun getPaymentMethods(): Data<PaymentMethodResponse> {
        return safeCallApi(mapper = { paymentMethods ->
            paymentMethods.data.forEach { paymentMethod ->
                paymentMethod.setPaymentMethodCode()
            }
            paymentMethods
        }) {
            apiService.getPaymentMethods()
        }
    }

    override suspend fun createOrder(
        userId: String,
        carts: List<Cart>,
        paymentMethod: PaymentMethod,
        voucher: Voucher?,
        address: String?
    ): Data<CreateOrderResponse> {
        return safeCallApi {
            apiService.createOrder(
                OrderRequest(
                    userId = userId,
                    cartId = carts.map { it.id },
                    paymentMethodId = paymentMethod.id,
                    voucherId = voucher?.id,
                    address = address
                )
            )
        }
    }

    override suspend fun getOrders(userId: String): Data<GetOrderResponse> {
        return safeCallApi(mapper = { orders ->
            orders.data.forEach { order ->
                order.paymentMethodId?.setPaymentMethodCode()
            }
            orders
        }) {
            apiService.getOrders(userId)
        }
    }

    override suspend fun cancelOrder(orderId: String): Data<BaseResponse> {
        return safeCallApi {
            apiService.cancelOrder(orderId)
        }
    }
}
