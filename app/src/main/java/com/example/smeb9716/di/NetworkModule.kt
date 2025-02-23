package com.example.smeb9716.di

import android.util.Log
import com.example.smeb9716.foundation.api.ApiService
import com.example.smeb9716.utils.DataManager
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = ""
    private const val KEY_AUTHORIZATION = "Authorization"
    private const val TOKEN_PREFIX = "Bearer "

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
//            .addInterceptor { chain ->
//                val newRequest = chain.request().newBuilder()
//                    .addHeader(KEY_AUTHORIZATION, TOKEN_PREFIX + DataManager.getInstance().bearerToken)
//                    .build()
//                chain.proceed(newRequest)
//            }
            .addInterceptor(CurlInterceptor(object : Logger {
                override fun log(message: String) {
                    Log.v("cURL", message)
                }
            }))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}