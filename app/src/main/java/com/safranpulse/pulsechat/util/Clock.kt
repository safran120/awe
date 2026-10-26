package com.safranpulse.pulsechat.util

/**
 * Abstracts time-related operations to make code testable.
 */
interface Clock {
    /** Returns current time in milliseconds since Unix epoch. */
    fun now(): Long
}

/**
 * Production implementation of Clock that uses system time.
 */
class SystemClock : Clock {
    override fun now(): Long = System.currentTimeMillis()
}

/**
 * Fixed clock implementation for testing.
 */
class FixedClock(private val fixedTimeMillis: Long) : Clock {
    override fun now(): Long = fixedTimeMillis
}