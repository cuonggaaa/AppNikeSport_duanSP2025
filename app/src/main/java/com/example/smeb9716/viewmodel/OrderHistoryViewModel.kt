package com.example.smeb9716.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smeb9716.foundation.BaseViewModel
import com.example.smeb9716.foundation.api.ApiRepository
import com.example.smeb9716.foundation.error.BGType
import com.example.smeb9716.model.Order
import com.example.smeb9716.model.WholeApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
) : BaseViewModel() {
    private val _orders = MutableLiveData<List<Order>>(mutableListOf())
    val orders get() = _orders

    val reviewSuccess = MutableLiveData(false)

    fun getOrders() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.getOrders(WholeApp.USER_ID)
            showLoading(false)
            handleResponse(response, {
                _orders.postValue(it?.data ?: mutableListOf())
            }, onError = {})
        }
    }

    fun cancelOrder(orderId: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.cancelOrder(orderId)
            showLoading(false)
            handleResponse(response, {
                handleMessage(
                    message = it?.message ?: "Hủy đơn hàng thành công",
                    bgType = BGType.BG_TYPE_SUCCESS
                )
                getOrders()
            }, onError = {})
        }
    }

    fun sendProductReview(productId: String, rating: Double, comment: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.reviewProduct(
                userId = WholeApp.USER_ID, productId = productId, rating = rating, content = comment
            )
            showLoading(false)

            handleResponse(response = response, onSuccess = {
                handleMessage(
                    message = it?.message ?: "Gửi đánh giá thành công",
                    bgType = BGType.BG_TYPE_SUCCESS
                )
                reviewSuccess.postValue(true)
            }, onError = { errorMsg ->
                // Handle error response
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
            })
        }
    }
}