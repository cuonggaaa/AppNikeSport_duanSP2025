package com.example.smeb9716.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smeb9716.foundation.BaseViewModel
import com.example.smeb9716.foundation.api.ApiRepository
import com.example.smeb9716.foundation.error.BGType
import com.example.smeb9716.model.User
import com.example.smeb9716.model.WholeApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val apiRepository: ApiRepository) :
    BaseViewModel() {
    private val _profile = MutableLiveData<User>()
    val profile: MutableLiveData<User> get() = _profile

    val changePasswordSuccess = MutableLiveData<Boolean>()

    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.getUserDetail(WholeApp.USER_ID)
            showLoading(false)

            handleResponse(response = response, onSuccess = {
                _profile.postValue(it?.user)
            }, onError = { errorMsg ->
                // Handle error response
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
            })
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.changePassword(
                WholeApp.USER_ID, oldPassword, newPassword
            )
            showLoading(false)

            handleResponse(response = response, onSuccess = {
                handleMessage(
                    message = it?.message ?: "Thay đổi mật khẩu thành công",
                    bgType = BGType.BG_TYPE_SUCCESS
                )
                changePasswordSuccess.postValue(true)
            }, onError = { errorMsg ->
                // Handle error response
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
            })

        }
    }
}
