package com.example.smeb9716.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smeb9716.foundation.BaseViewModel
import com.example.smeb9716.foundation.api.ApiRepository
import com.example.smeb9716.model.Cart
import com.example.smeb9716.model.WholeApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    @ApplicationContext private val application: Context, private val apiRepository: ApiRepository,
) : BaseViewModel() {
    private val _cartLiveData = MutableLiveData(emptyList<Cart>().toMutableList())
    val cartLiveData get() = _cartLiveData

    private val totalPriceLiveData = MutableLiveData(0L)
    val totalPrice get() = totalPriceLiveData

    private val totalQuantityLiveData = MutableLiveData(0)
    val totalQuantity get() = totalQuantityLiveData

    val isTotalSelected: Boolean
        get() {
            return _cartLiveData.value?.all { it.selected == true } ?: false
        }

    fun getSelectedCarts(): List<Cart> {
        return _cartLiveData.value?.filter { it.selected == true } ?: emptyList()
    }

    fun getCart() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.getCarts(WholeApp.USER_ID)
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                _cartLiveData.postValue(it?.data?.toMutableList())
                calculateTotalPrice()
            }, onError = {
                _cartLiveData.postValue(emptyList<Cart>().toMutableList())
            })
        }
    }

    fun selectAll(selected: Boolean) {
        _cartLiveData.value?.forEach { it.selected = selected }
        _cartLiveData.postValue(_cartLiveData.value)
        calculateTotalPrice()
    }

    fun selectItem(cart: Cart, selected: Boolean) {
        cart.selected = selected
        _cartLiveData.postValue(_cartLiveData.value)
        calculateTotalPrice()
    }

    fun deleteCart(cart: Cart) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.deleteCart(cart.id)
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                _cartLiveData.value?.remove(cart)
                _cartLiveData.postValue(_cartLiveData.value)
                calculateTotalPrice()
            }, onError = {
                // Do nothing
            })
        }
    }

    fun increaseQuantity(cart: Cart) {
        val availableSize = cart.productId.sizes?.find { it.size == cart.size }
        if (availableSize != null && cart.quantity < availableSize.quantity) {
            updateQuantity(cart, cart.quantity + 1)
        }
    }

    fun decreaseQuantity(cart: Cart) {
        if (cart.quantity > 1) {
            updateQuantity(cart, cart.quantity - 1)
        }
    }

    private fun updateQuantity(cart: Cart, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.updateCart(cart.id, quantity)
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                cart.quantity = quantity
                _cartLiveData.postValue(_cartLiveData.value)
                calculateTotalPrice()
            }, onError = {
                // Do nothing
            })
        }
    }

    private fun calculateTotalPrice() {
        var totalPrice = 0L
        var totalQuantity = 0
        _cartLiveData.value?.forEach {
            if (it.selected == true) {
                totalPrice += it.productId.getDiscountedPrice() * it.quantity
                totalQuantity += it.quantity
            }
        }
        totalPriceLiveData.postValue(totalPrice)
        totalQuantityLiveData.postValue(totalQuantity)
    }
}
