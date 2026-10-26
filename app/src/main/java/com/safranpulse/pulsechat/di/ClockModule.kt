package com.safranpulse.pulsechat.di

import com.safranpulse.pulsechat.util.Clock
import com.safranpulse.pulsechat.util.FixedClock
import com.safranpulse.pulsechat.util.SystemClock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing Clock implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
object ClockModule {

    /**
     * Provides the system clock for production use.
     */
    @Provides
    @Singleton
    fun provideSystemClock(): Clock = SystemClock()

    /**
     * Provides a fixed clock for testing (returns a fixed time).
     * Useful for deterministic tests.
     */
    @Provides
    @Singleton
    fun provideFixedClock(fixedTimeMillis: Long = System.currentTimeMillis()): Clock {
        return FixedClock(fixedTimeMillis)
    }
}