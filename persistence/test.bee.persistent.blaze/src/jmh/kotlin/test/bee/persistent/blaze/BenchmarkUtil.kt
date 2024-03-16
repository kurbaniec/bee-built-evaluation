package test.bee.persistent.blaze

import kotlin.math.pow

/**
 *
 *
 * @author Kacper Urbaniec
 * @version 2024-03-16
 */
class BenchmarkUtil {

    companion object {

        @JvmStatic
        fun testFromKotlin(): Int {
            return 100.toDouble().pow(10).toInt()
        }
    }

}