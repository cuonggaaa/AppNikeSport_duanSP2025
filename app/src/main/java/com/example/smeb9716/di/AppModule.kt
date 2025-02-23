package com.example.smeb9716.di

import android.content.Context
import com.example.smeb9716.utils.BrainApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApplicantRepository(@ApplicationContext context: Context): BrainApp {
        return context as BrainApp
    }
}