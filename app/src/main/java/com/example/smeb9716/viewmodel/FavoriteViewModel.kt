package com.example.smeb9716.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smeb9716.foundation.BaseViewModel
import com.example.smeb9716.foundation.api.ApiRepository
import com.example.smeb9716.foundation.error.BGType
import com.example.smeb9716.model.Product
import com.example.smeb9716.model.WholeApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    @ApplicationContext private val application: Context, private val apiRepository: ApiRepository,
) : BaseViewModel() {
    private val _products = MutableLiveData<MutableList<Product>?>(null)
    val products get() = _products

    fun getProducts() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.getProducts()
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                it?.data?.data?.let { it1 -> getFavoriteProductIds(it1) }
            }, onError = { errorMsg ->
                // Handle error response
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
            })
        }
    }

    private fun getFavoriteProductIds(products: List<Product>) {
        val productList = products.toMutableList()
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.getFavoriteProducts(WholeApp.USER_ID)
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                it?.data?.forEach { favoriteProduct ->
                    productList.find { it.id == favoriteProduct.product.id }?.isFavorite = true
                }

                _products.postValue(productList)
            }, onError = { errorMsg ->
                _products.postValue(productList)
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
                _products.value?.find { it.id == product.id }?.isFavorite = true
                _products.postValue(_products.value)

            }, onError = { errorMsg ->
                // Handle error response
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
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
                _products.value?.find { it.id == product.id }?.isFavorite = false
                _products.postValue(_products.value)
            }, onError = { errorMsg ->
                // Handle error response
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
            })
        }
    }
}
