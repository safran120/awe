package com.safranpulse.pulsechat.di

import android.content.Context
import com.safranpulse.pulsechat.data.CallRepository
import com.safranpulse.pulsechat.data.ChatRepository
import com.safranpulse.pulsechat.data.UserRepository
import com.safranpulse.pulsechat.data.local.AppDatabase
import com.safranpulse.pulsechat.data.local.MessageDao
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
    fun provideUserRepository(): UserRepository = UserRepository()

    @Provides
    @Singleton
    fun provideChatRepository(
        @ApplicationContext context: Context,
        messageDao: MessageDao
    ): ChatRepository = ChatRepository(context, messageDao)

    @Provides
    @Singleton
    fun provideCallRepository(): CallRepository = CallRepository()

    @Provides
    @Singleton
    fun provideMessageDao(@ApplicationContext context: Context): MessageDao =
        AppDatabase.get(context).messageDao()
}
