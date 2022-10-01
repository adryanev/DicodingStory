package dev.adryanev.dicodingstory.core.presentations

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun SwipeRefreshLayout.flowRefresh(): Flow<Unit> = callbackFlow {
    var listener: SwipeRefreshLayout.OnRefreshListener? = SwipeRefreshLayout.OnRefreshListener {
        trySend(Unit)
    }

    setOnRefreshListener(listener)
    awaitClose {
        listener = null
        setOnRefreshListener(null)
    }
}