package com.example.smeb9716.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smeb9716.foundation.BaseViewModel
import com.example.smeb9716.foundation.api.ApiRepository
import com.example.smeb9716.foundation.error.BGType
import com.example.smeb9716.model.User
import com.example.smeb9716.model.WholeApp
import com.example.smeb9716.model.request.UpdateProfileRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
) : BaseViewModel() {

    private val _user = MutableLiveData<User>()
    val updateSuccess = MutableLiveData<Boolean>()

    val user get() = _user
    fun getProfileDetail() {
        job = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.getUserDetail(WholeApp.USER_ID)
            showLoading(false)
            handleResponse(response, onSuccess = {
                _user.postValue(it?.user)
            }, onError = { errorMsg ->
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
            })
        }
    }

    fun updateProfile(request: UpdateProfileRequest) {
        job = viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            showLoading(true)
            val response = apiRepository.updateUser(WholeApp.USER_ID, request)
            showLoading(false)
            handleResponse(response, onSuccess = {
                updateSuccess.postValue(true)
            }, onError = { errorMsg ->
                handleMessage(
                    message = errorMsg ?: "Lỗi không xác định", bgType = BGType.BG_TYPE_ERROR
                )
            })
        }
    }
}