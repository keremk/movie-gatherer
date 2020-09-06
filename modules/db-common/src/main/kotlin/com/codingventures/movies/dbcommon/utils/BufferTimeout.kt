package com.codingventures.movies.dbcommon.utils

import java.time.Duration
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.selects.select

// From a blog post by Pin-Sho Feng: https://dev.to/psfeng/a-story-of-building-a-custom-flow-operator-buffertimeout-4d95

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
fun <T> Flow<T>.bufferTimeout(size: Int, duration: Duration): Flow<List<T>> {
    require(size > 0) { "Window size should be greater than 0" }
    require(duration.toMillis() > 0) { "Duration should be greater than 0" }

    return flow {
        coroutineScope {
            val events = ArrayList<T>(size)
            val tickerChannel = ticker(duration.toMillis())
            try {
                val upstreamValues = produce { collect { send(it) } }

                while (isActive) {
                    var hasTimedOut = false

                    select<Unit> {
                        upstreamValues.onReceive {
                            events.add(it)
                        }

                        tickerChannel.onReceive {
                            hasTimedOut = true
                        }
                    }

                    if (events.size == size || (hasTimedOut && events.isNotEmpty())) {
                        emit(events)
                        events.clear()
                    }
                }
            } catch (e: ClosedReceiveChannelException) {
                // drain remaining events
                if (events.isNotEmpty()) emit(events)
            } finally {
                tickerChannel.cancel()
            }
        }
    }
}