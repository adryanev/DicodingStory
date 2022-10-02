package dev.adryanev.dicodingstory.core.presentations

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.adryanev.dicodingstory.core.utils.ViewConstants
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

fun View.setSingleClick(execution: () -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        var lastClickTime: Long = 0

        override fun onClick(p0: View?) {
            if (System.currentTimeMillis() - lastClickTime < ViewConstants.THRESHOLD_CLICK_TIME) return
            lastClickTime = System.currentTimeMillis()
            execution.invoke()
        }
    })
}
