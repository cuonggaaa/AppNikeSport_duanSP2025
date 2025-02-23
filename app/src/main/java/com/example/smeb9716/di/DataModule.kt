package com.example.smeb9716.di

import com.example.smeb9716.foundation.api.ApiRepository
import com.example.smeb9716.foundation.api.ApiRepositoryImpl
import com.example.smeb9716.foundation.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideRepository(service: ApiService): ApiRepository {
        return ApiRepositoryImpl(service)
    }
}