package com.smach.zapmancer.core.common.utils

interface Paginator<Key, Item> {
    suspend fun loadNextItems()

    fun reset()
}

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private val onLoadUpdated: (Boolean) -> Unit,
    private val onRequest: suspend (nextKey: Key) -> Result<List<Item>, DataError.Network>,
    private val getNextKey: suspend (List<Item>) -> Key,
    private val onError: suspend (DataError.Network, Throwable?) -> Unit,
    private val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit,
) : Paginator<Key, Item> {
    private var currentKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if (isMakingRequest) return

        isMakingRequest = true
        onLoadUpdated(true)

        val result = onRequest(currentKey)
        isMakingRequest = false

        when (result) {
            is Result.Success -> {
                val items = result.data
                currentKey = getNextKey(items)
                onSuccess(items, currentKey)
                onLoadUpdated(false)
            }

            is Result.Error -> {
                onError(result.error, result.throwable)
                onLoadUpdated(false)
            }
        }
    }

    override fun reset() {
        currentKey = initialKey
        isMakingRequest = false
        onLoadUpdated(false)
    }
}
