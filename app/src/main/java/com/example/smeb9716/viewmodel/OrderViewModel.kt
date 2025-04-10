package com.example.smeb9716.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smeb9716.foundation.BaseViewModel
import com.example.smeb9716.foundation.api.ApiRepository
import com.example.smeb9716.foundation.error.BGType
import com.example.smeb9716.model.Cart
import com.example.smeb9716.model.PaymentMethod
import com.example.smeb9716.model.Voucher
import com.example.smeb9716.model.WholeApp
import com.example.smeb9716.model.response.CreateOrderResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    @ApplicationContext private val application: Context, private val apiRepository: ApiRepository,
) : BaseViewModel() {
    private val _vouchersLiveData = MutableLiveData(emptyList<Voucher>().toMutableList())
    val vouchersLiveData get() = _vouchersLiveData

    private val _addressLiveData = MutableLiveData("")
    val addressLiveData get() = _addressLiveData

    private val _paymentMethod = MutableLiveData(emptyList<PaymentMethod>().toMutableList())
    val paymentMethod get() = _paymentMethod

    private val _selectedPaymentMethod = MutableLiveData<PaymentMethod?>(null)
    val selectedPaymentMethod get() = _selectedPaymentMethod

    private val _createdOrderLiveData = MutableLiveData<CreateOrderResponse?>(null)
    val createdOrderLiveData get() = _createdOrderLiveData

    private val _paymentValue = MutableLiveData(0L)
    val paymentValue get() = _paymentValue

    private val _totalPrice = MutableLiveData(0L)
    val totalPrice get() = _totalPrice

    val discountedPrice
        get() = calculateDiscountedPrice()

    private fun calculateDiscountedPrice(): Long {
        _vouchersLiveData.value?.forEach { it: Voucher ->
            if (it.selected == true) {
                return it.discountValue ?: 0L
            }
        }
        return 0L
    }

    private val _carts: MutableList<Cart> = mutableListOf()
    var carts
        get() = _carts
        set(value) {
            _carts.clear()
            _carts.addAll(value)
            calculateTotalPrice()
        }

    fun getVoucher() {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            showLoading(true)
            val response = apiRepository.getVouchers()
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                val active = it?.data?.filter { it.status == "Active" }
                _vouchersLiveData.postValue(active?.toMutableList())
            }, onError = {
                // Do something
            })
        }
    }

    fun getProfile() {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            showLoading(true)
            val response = apiRepository.getUserDetail(WholeApp.USER_ID)
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                _addressLiveData.postValue(it?.user?.address)
            }, onError = {
                // Do something
            })
        }
    }

    fun createOrder() {
        if (_selectedPaymentMethod.value == null) {
            showLoading(false)
            return
        }

        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            showLoading(true)

            val response = apiRepository.createOrder(
                WholeApp.USER_ID,
                carts,
                _selectedPaymentMethod.value!!,
                _vouchersLiveData.value?.find { it.selected == true },
                _addressLiveData.value
            )
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                // Do something
                _createdOrderLiveData.postValue(it)
            }, onError = { errorMsg ->
                // Do something
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_WARNING
                )
            })
        }
    }

    fun selectPaymentMethod(paymentMethod: PaymentMethod? = null) {
        _selectedPaymentMethod.postValue(paymentMethod)
        calculatePaymentValue()
    }

    fun getPaymentMethod() {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            showLoading(true)
            val response = apiRepository.getPaymentMethods()
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                _paymentMethod.postValue(it?.data?.toMutableList())
                _selectedPaymentMethod.postValue(it?.data?.firstOrNull())
            }, onError = {
                // Do something
            })
        }
    }

    fun applyVoucher(voucher: Voucher) {
        _vouchersLiveData.value?.forEach {
            it.selected = it.id == voucher.id
        }
        _vouchersLiveData.postValue(_vouchersLiveData.value)
        calculatePaymentValue()
    }

    private fun calculateTotalPrice() {
        val totalPrice = _carts.sumOf { it.productId.getDiscountedPrice() * it.quantity }
        _totalPrice.postValue(totalPrice)
        calculatePaymentValue()
    }

    private fun calculatePaymentValue() {
        calculateDiscountedPrice()
        Timber.d("Discounted price: $discountedPrice")
        _paymentValue.postValue(((_totalPrice.value ?: 0L) - discountedPrice).coerceAtLeast(0L))
    }
}