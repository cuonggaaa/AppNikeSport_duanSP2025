package com.example.smeb9716.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smeb9716.foundation.BaseViewModel
import com.example.smeb9716.foundation.api.ApiRepository
import com.example.smeb9716.foundation.error.BGType
import com.example.smeb9716.model.Cart
import com.example.smeb9716.model.Notification
import com.example.smeb9716.model.Product
import com.example.smeb9716.model.WholeApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @ApplicationContext private val application: Context, private val apiRepository: ApiRepository,
) : BaseViewModel() {
    private val _notifications = MutableLiveData<List<Notification>>(emptyList<Notification>().toMutableList())
    val notifications get() = _notifications


    fun getNotifications() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.getNotifications(WholeApp.USER_ID)
            showLoading(false)
            handleResponse(response = response, onSuccess = {
                _notifications.postValue(it?.data ?: mutableListOf())
            }, onError = { errorMsg ->
                // Handle error response
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
            })
        }
    }
}
