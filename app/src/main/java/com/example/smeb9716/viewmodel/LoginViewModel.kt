package com.example.smeb9716.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smeb9716.foundation.BaseViewModel
import com.example.smeb9716.foundation.api.ApiRepository
import com.example.smeb9716.foundation.error.BGType
import com.example.smeb9716.model.WholeApp
import com.example.smeb9716.model.request.LoginRequest
import com.example.smeb9716.model.response.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
) : BaseViewModel() {
    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }

    private val _loginResponse = MutableLiveData<LoginResponse?>(null)
    val loginResponse: LiveData<LoginResponse?> get() = _loginResponse

    val isSplashLoginFail = MutableLiveData(false)

    fun login(username: String, password: String) {
        job = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.login(LoginRequest(username, password))
            showLoading(false)

            handleResponse(response = response, onSuccess = {
                _loginResponse.postValue(it)
                WholeApp.USER_ID = it?.user?.id ?: ""
            }, onError = { errorMsg ->
                // Handle error response
                isSplashLoginFail.postValue(true)
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
            })
        }
    }
}
