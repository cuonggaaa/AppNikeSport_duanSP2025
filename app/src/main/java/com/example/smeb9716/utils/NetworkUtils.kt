package com.example.smeb9716.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkUtils {

    companion object {
        /**
         * ネットワーク接続判定処理
         *
         * @param context Context
         * @return true:ネットワーク接続有 false:ネットワーク接続無
         */
        fun isConnected(context: Context): Boolean {
            val connectManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities =
                connectManager.getNetworkCapabilities(connectManager.activeNetwork)

            return if (networkCapabilities == null) {
                false
            } else {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
        }
    }
}