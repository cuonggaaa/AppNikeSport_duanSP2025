package com.example.smeb9716.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smeb9716.foundation.BaseViewModel
import com.example.smeb9716.foundation.api.ApiRepository
import com.example.smeb9716.foundation.error.BGType
import com.example.smeb9716.model.Banner
import com.example.smeb9716.model.Category
import com.example.smeb9716.model.Product
import com.example.smeb9716.model.Voucher
import com.example.smeb9716.model.WholeApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val application: Context, private val apiRepository: ApiRepository
) : BaseViewModel() {
    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
    }

    private val _bannerLiveData = MutableLiveData<MutableList<Banner>?>(null)
    val bannerLiveData get() = _bannerLiveData

    private val _categoryLiveData = MutableLiveData<MutableList<Category>?>(null)
    val categoryLiveData get() = _categoryLiveData

    private val _products = MutableLiveData<MutableList<Product>?>(null)
    val products get() = _products

    private val _vouchers = MutableLiveData<MutableList<Voucher>?>(null)
    val vouchers get() = _vouchers

    private val _cartCount = MutableLiveData(0)
    val cartCount get() = _cartCount

    fun getBanners() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.getBanners()
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                _bannerLiveData.postValue(it?.data?.data?.toMutableList())
            }, onError = { errorMsg ->
                // Handle error response
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
            })
        }
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.getCategories()
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                _categoryLiveData.postValue(it?.data?.data?.toMutableList())
            }, onError = { errorMsg ->
                // Handle error response
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
            })
        }
    }



    fun getVouchers() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.getVouchers()
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                // Handle success response
                _vouchers.postValue(it?.data?.toMutableList())
            }, onError = { errorMsg ->
                // Handle error response
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
            })
        }
    }


}
