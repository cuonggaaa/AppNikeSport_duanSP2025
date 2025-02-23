package com.example.smeb9716.utils

class DataManager private constructor() {

    var packageName: String = "com.example.smeb9716"
    var bearerToken: String? = null

    companion object {
        @Volatile
        private var instance: DataManager? = null

        fun getInstance(): DataManager =
            instance ?: synchronized(this) {
                instance ?: DataManager().also { instance = it }
            }
    }
}