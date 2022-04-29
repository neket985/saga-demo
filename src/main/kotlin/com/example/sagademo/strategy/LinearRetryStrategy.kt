package com.example.sagademo.strategy

import java.time.Duration

class LinearRetryStrategy(private val initDuration: Duration, private val scale: Double = 1.0) : RetryStrategy {
    init {
        if (scale <= 0) error("Scale must be positive")
    }

    override fun getNextTime(triesCount: Int): Duration =
        Duration.ofSeconds((initDuration.seconds * triesCount * scale).toLong())
}