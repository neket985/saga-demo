package com.example.sagademo.strategy

import java.time.Duration

interface RetryStrategy {
    fun getNextTime(triesCount: Int): Duration

    companion object {
        fun printDurationsForFirstNTries(n: Int, starategy: RetryStrategy) {
            if (n > 1000) error("n is too long")
            println("| N | Duration | Total |")
            var totalDuration = Duration.ofSeconds(0)
            (0..n).forEach {
                val duration = starategy.getNextTime(it)
                totalDuration = totalDuration.plus(duration)
                println("| $it | $duration | $totalDuration |")
            }
        }
    }
}