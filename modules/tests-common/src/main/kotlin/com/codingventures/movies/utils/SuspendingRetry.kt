package com.codingventures.movies.utils

class Retryable<T>(
    internal val times: Int = 100,
    internal val maxDelay: Long = 200,
    internal val factor: Double = 2.0,
    internal val what: suspend () -> T
)

fun <T> retrySuspending(
    times: Int = 100,
    maxDelay: Long = 200,
    factor: Double = 2.0,
    what: suspend () -> T
): Retryable<T> {
    return Retryable(times, maxDelay, factor, what)
}

suspend infix fun <T> Retryable<T>.until(until: RetryCondition<T>): T {
    var currentDelay = 100L
    repeat(times - 1) {
        val result = what()
        if (until(result)) {
            return result
        }

        Thread.sleep(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }

    throw ConditionNotMetException()
}

typealias RetryCondition<T> = (T) -> Boolean

fun <T : List<*>> sizeEquals(expectedSize: Int): RetryCondition<T> = { list -> list.size == expectedSize }
fun <T> equalTo(expectedValue: T): RetryCondition<T> = { it == expectedValue }
fun <T> isNot(previous: T?): RetryCondition<T?> = { it != previous }
fun <T> isNotIn(previous: List<T>): RetryCondition<T> = { it !in previous }
fun <T> isNotNull(): RetryCondition<T> = { it != null }
fun <T> isNull(): RetryCondition<T> = { it == null }
fun <T : List<*>> noneIn(previous: T): RetryCondition<T> = { it.intersect(previous).isEmpty() }
fun <T : List<*>> allIn(previous: T): RetryCondition<T> = { it == previous }

private class ConditionNotMetException : Throwable()
