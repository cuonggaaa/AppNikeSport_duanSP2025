package com.example.smeb9716.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smeb9716.foundation.BaseViewModel
import com.example.smeb9716.foundation.api.ApiRepository
import com.example.smeb9716.model.Voucher
import com.example.smeb9716.model.WholeApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GiftViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiRepository: ApiRepository,
) : BaseViewModel() {
    private val _vouchers = MutableLiveData<List<Voucher>?>(null)
    val vouchers get() = _vouchers

    fun getVoucher() {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            showLoading(true)
            val response = apiRepository.getVouchers()
            showLoading(false)
            handleResponse(response, onSuccess = {
                _vouchers.postValue(it?.data)
                Timber.d("Vouchers: ${it?.data}")
            }, onError = {

            })
        }
    }

    fun getCartCount() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.getCarts(WholeApp.USER_ID)
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                // Handle success response
                WholeApp.cartCount.postValue(it?.data?.size ?: 0)
            }, onError = { errorMsg ->
                // Handle error response
                WholeApp.cartCount.postValue(0)
            })
        }
    }
}