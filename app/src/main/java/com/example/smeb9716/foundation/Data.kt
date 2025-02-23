package com.example.smeb9716.foundation

sealed class Data<out T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Data<T>(data = data)
    class Error(message: String) : Data<Nothing>(message = message)
}
