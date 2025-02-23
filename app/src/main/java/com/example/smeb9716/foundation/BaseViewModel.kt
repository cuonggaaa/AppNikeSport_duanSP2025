package com.example.smeb9716.foundation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smeb9716.foundation.api.ApiRepository
import com.example.smeb9716.foundation.error.BGType
import com.example.smeb9716.foundation.error.ResponseMessage
import com.example.smeb9716.model.ErrorMessage
import com.example.smeb9716.utils.BrainApp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseViewModel: ViewModel() {
    var job: Job? = null

    var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        // show message
        handleMessage(
            message = throwable.message ?: "Lỗi không xác định",
            bgType = BGType.BG_TYPE_ERROR
        )
    }

    fun handleMessage(message: String, bgType: BGType) {
        viewModelScope.launch {
            _responseMessage.emit(
                ResponseMessage(
                    message = message,
                    bgType = bgType
                )
            )
        }
    }

    suspend fun <T> handleResponse(
        response: Data<T>,
        onSuccess: suspend (T?) -> Unit,
        onError: suspend (String?) -> Unit
    ) {
        when (response) {
            is Data.Success -> {
                onSuccess.invoke(response.data)
            }

            is Data.Error -> {
                onError.invoke(response.message)
            }
        }
    }

    private val _responseMessage = MutableSharedFlow<ResponseMessage>()
    val responseMessage: SharedFlow<ResponseMessage> = _responseMessage

    private val _isLoading = MutableSharedFlow<Boolean>()
    val isLoading: SharedFlow<Boolean> = _isLoading

    fun showLoading(isShow: Boolean) {
        viewModelScope.launch {
            _isLoading.emit(isShow)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        job = null
    }
}