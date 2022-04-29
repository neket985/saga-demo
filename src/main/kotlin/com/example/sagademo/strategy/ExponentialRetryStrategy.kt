package com.example.sagademo.strategy

import java.time.Duration

/**
 * t(x) = a*b^(x/c)
 * где
 *      x - номер попытки (@triesCount)
 *      a - значение задержки для первой попытки (@startDuration)
 *      b - индекс роста. во сколько быстро будет расти задержка в зависимости от количества попыток (@startDuration)
 *      c - постоянная, обозначающая, сколько попыток надо совершить,
 *          чтобы задержка увеличилась на один коэффициент b (@rarityConst)
 *      t - время задержки на x-ую попытку (@return)
 *
 *
 *      если мы хотим, чтобы 100 попыток занимали 24 часа
 *      получаем, что интеграл по нашей функции на отрезке от 0 до 100 должен равняться 24 часам
 *      a*b^(100/c)
 */
class ExponentialRetryStrategy(
    private val startDuration: Duration,
    private val growIndex: Double,
    private val rarityConst: Double
) : RetryStrategy {
    init {
        if (growIndex < 1.0) error("growIndex must be greater than 1. Grow function would not be grown :)")
        if (growIndex == 1.0) error("growIndex must be greater than 1. Use LinearRetryStrategy instead")
    }

    override fun getNextTime(triesCount: Int): Duration =
        Duration.ofSeconds(
            startDuration.seconds * (Math.pow(growIndex, triesCount / rarityConst)).toLong()
        )

}