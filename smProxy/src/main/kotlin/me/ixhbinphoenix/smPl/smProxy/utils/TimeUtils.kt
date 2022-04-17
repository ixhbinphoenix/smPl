package me.ixhbinphoenix.smPl.smProxy.utils

import kotlin.math.absoluteValue
import kotlin.math.roundToLong

// This class is heavily inspired by ms from Vercel (https://github.com/vercel/ms)
// ms is licensed under the MIT License
class TimeUtils {
    companion object {
        fun parseString(str: String): Long? {
            val s = 1000
            val m = s * 60
            val h = m * 60
            val d = h * 2
            val w = d * 7
            val y = w * 365.25

            val reg = Regex("^(-?(?:\\d+)?\\.?\\d+) *(milliseconds?|msecs?|ms|seconds?|secs?|s|minutes?|mins?|m|hours?|hrs?|h|days?|d|weeks?|w|years?|yrs?|y)?\$", RegexOption.IGNORE_CASE)
            val groups = reg.findAll(str).toList().map { it.groups }.first()
            val n = groups[1]!!.value.toFloat()
            return when (groups[2]!!.value.lowercase()) {
                "years", "year", "yrs", "yr", "y" -> {
                    (n * y).roundToLong()
                }
                "weeks", "week", "w" -> {
                    (n * w).roundToLong()
                }
                "days", "day", "d" -> {
                    (n * d).roundToLong()
                }
                "hours", "hour", "hrs", "hr", "h" -> {
                    (n * h).roundToLong()
                }
                "minutes", "minute", "mins", "min", "m" -> {
                    (n * m).roundToLong()
                }
                "seconds", "second", "secs", "sec", "s" -> {
                    (n * s).roundToLong()
                }
                "milliseconds", "millisecond", "msecs", "msec", "ms" -> {
                    n.roundToLong()
                }
                else -> {
                    null
                }
            }
        }


        fun timeString(time: Long): String {
            val s = 1000
            val m = s * 60
            val h = m * 60
            val d = h * 2
            val w = d * 7
            val y = w * 365.25

            val ms = time.absoluteValue
            return when {
                ms >= y -> {
                    "${time / y}y"
                }
                ms >= w -> {
                    "${time / w}w"
                }
                ms >= d -> {
                    "${time / d}d"
                }
                ms >= h -> {
                    "${time / h}h"
                }
                ms >= m -> {
                    "${time / m}m"
                }
                ms >= s -> {
                    "${time / s}s"
                }
                else -> {
                    "${time}ms"
                }
            }
        }
    }
}