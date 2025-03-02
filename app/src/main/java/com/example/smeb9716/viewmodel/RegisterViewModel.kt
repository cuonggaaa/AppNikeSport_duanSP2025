package com.example.smeb9716.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smeb9716.foundation.BaseViewModel
import com.example.smeb9716.foundation.api.ApiRepository
import com.example.smeb9716.foundation.error.BGType
import com.example.smeb9716.model.request.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : BaseViewModel() {
    private val _registerSuccess = MutableLiveData(false)
    val registerSuccess: LiveData<Boolean> get() = _registerSuccess

    fun registerUser(request: RegisterRequest) {
        job = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.register(request)
            showLoading(false)

            handleResponse(response = response, onSuccess = {
                Timber.d("Register success")
                _registerSuccess.postValue(true)
            }, onError = {
                handleMessage(
                    message = it ?: "Error", bgType = BGType.BG_TYPE_ERROR
                )
            })
        }
    }
}
