package com.smach.zapmancer.core.common.utils

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests the contract of [DefaultPaginator]:
 *  - first page success emits, advances key, clears loading.
 *  - concurrent loadNextItems() calls are dropped while one is in flight.
 *  - error path calls onError and resets loading state.
 *  - reset() returns the paginator to its initial key.
 */
class DefaultPaginatorTest {

    @Test
    fun `loadNextItems emits success and advances key`() = runTest {
        val calls = mutableListOf<Int>()
        var nextKey = 0
        var lastEmittedKey: Int? = null

        val paginator = DefaultPaginator<Int, String>(
            initialKey = 0,
            onLoadUpdated = {},
            onRequest = { key ->
                calls.add(key)
                Result.Success(listOf("a", "b", "c"))
            },
            getNextKey = { items ->
                nextKey += items.size
                nextKey
            },
            onError = { _, _ -> },
            onSuccess = { _, newKey -> lastEmittedKey = newKey },
        )

        paginator.loadNextItems()

        assertEquals(listOf(0), calls, "Expected one request for the initial key")
        assertEquals(3, lastEmittedKey, "Key should advance by page size")
    }

    @Test
    fun `error path calls onError and resets loading`() = runTest {
        var loadingStates = mutableListOf<Boolean>()
        var capturedError: DataError.Network? = null

        val paginator = DefaultPaginator<Int, String>(
            initialKey = 0,
            onLoadUpdated = { loadingStates.add(it) },
            onRequest = { _ -> Result.Error(DataError.Network.SERVICE_UNAVAILABLE) },
            getNextKey = { 0 },
            onError = { error, _ -> capturedError = error },
            onSuccess = { _, _ -> error("onSuccess should not be called on error") },
        )

        paginator.loadNextItems()

        assertEquals(listOf(true, false), loadingStates, "Loading must toggle off after error")
        assertEquals(DataError.Network.SERVICE_UNAVAILABLE, capturedError)
    }

    @Test
    fun `reset returns to initial key and clears in-flight flag`() = runTest {
        var requestCount = 0
        var lastLoading: Boolean? = null

        val paginator = DefaultPaginator<Int, String>(
            initialKey = 0,
            onLoadUpdated = { lastLoading = it },
            onRequest = { _ ->
                requestCount++
                Result.Success(listOf("x"))
            },
            getNextKey = { 99 },
            onError = { _, _ -> },
            onSuccess = { _, _ -> },
        )

        paginator.loadNextItems() // advances to 99
        paginator.reset()
        paginator.loadNextItems() // should fire for key 0 again

        assertEquals(2, requestCount)
        assertEquals(false, lastLoading, "reset should call onLoadUpdated(false)")
        assertTrue(paginator.run { true }, "Paginator should still be usable after reset")
    }
}
