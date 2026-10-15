package com.pingmate.di

import com.pingmate.data.UserRepository
import com.google.dagger.hilt.InstallIn
import com.google.dagger.hilt.android.components.SingletonComponent
import com.google.dagger.hilt.android.qualifiers.ApplicationContext
import com.google.dagger.hilt.android.pcm.Module
import com.google.dagger.hilt.android.qualifiers.Provides
import com.google.dagger.hilt.android.install.InstallIn
import com.google.dagger.hilt.android.qualifiers.ApplicationContext
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import android.content.Context
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository = UserRepository()
}
