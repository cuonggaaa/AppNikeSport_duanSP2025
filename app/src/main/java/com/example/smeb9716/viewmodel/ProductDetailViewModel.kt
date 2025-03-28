package com.example.smeb9716.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smeb9716.foundation.BaseViewModel
import com.example.smeb9716.foundation.api.ApiRepository
import com.example.smeb9716.foundation.error.BGType
import com.example.smeb9716.model.Product
import com.example.smeb9716.model.WholeApp
import com.example.smeb9716.model.response.ProductReview
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    @ApplicationContext private val application: Context, private val apiRepository: ApiRepository,
) : BaseViewModel() {
    private val _product = MutableLiveData<Product?>(null)
    val product get() = _product

    private val _reviews = MutableLiveData<MutableList<ProductReview>?>(null)
    val reviews get() = _reviews

    private val _quantity = MutableLiveData(1)
    var quantity: Int
        get() = _quantity.value ?: 0
        set(value) {
            _quantity.postValue(value)
        }
    val quantityLiveData get() = _quantity

    val addCartSuccess = MutableLiveData(false)

    fun increaseQuantity() {
        if (quantity + 1 <= (product.value?.quantity ?: 0)) {
            quantity++
        }
    }

    fun decreaseQuantity() {
        if (quantity - 1 >= 1) {
            quantity--
        }
    }

    fun getProductDetail(productId: String) {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            showLoading(true)
            val response = apiRepository.getProductDetail(productId)
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                _product.postValue(it?.data)
                Timber.d("Product: ${it?.data}")

                if (quantity > (it?.data?.quantity ?: 0)) {
                    quantity = it?.data?.quantity ?: 0
                }
                isProductFavorite(it?.data ?: return@handleResponse)
            }, onError = { errorMsg ->
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
            })
        }
    }

    fun getProductReviews(productId: String) {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            showLoading(true)
            val response = apiRepository.getProductReviews(productId)
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                _reviews.postValue(it?.data?.toMutableList())
            }, onError = { errorMsg ->
            })
        }
    }

    private fun isProductFavorite(product: Product) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.getFavoriteProducts(WholeApp.USER_ID)
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                it?.data?.forEach { favoriteProduct ->
                    if (favoriteProduct.product.id == product.id) {
                        product.isFavorite = true
                        _product.postValue(product)
                        return@forEach
                    }
                }

            }, onError = { errorMsg ->
                _product.postValue(product)
            })
        }
    }

    fun addFavoriteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.addFavoriteProduct(WholeApp.USER_ID, product.id)
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                product.isFavorite = true
                _product.postValue(product)

            }, onError = { errorMsg ->
                // Handle error response
            })
        }
    }

    fun removeFavoriteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)

            val response = apiRepository.removeFavoriteProduct(
                userId = WholeApp.USER_ID, productId = product.id
            )

            showLoading(false)
            handleResponse(response = response, onSuccess = {
                product.isFavorite = false
                _product.postValue(product)
            }, onError = { errorMsg ->
                // Handle error response
            })
        }
    }

    fun getCartCount() {

    }

    fun addCart(product: Product, quantity: Int) {

    }
}
