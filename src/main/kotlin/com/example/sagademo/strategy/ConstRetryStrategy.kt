package com.example.sagademo.strategy

import java.time.Duration

class ConstRetryStrategy(private val constDuration: Duration) : RetryStrategy {
    override fun getNextTime(triesCount: Int): Duration = constDuration
}