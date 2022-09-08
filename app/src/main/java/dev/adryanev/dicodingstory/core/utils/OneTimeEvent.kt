package dev.adryanev.dicodingstory.core.utils

import java.util.concurrent.atomic.AtomicBoolean

/**
 * Represent One-Shot UI event
 */
data class OneTimeEvent <T> (val payload: T? = null){

    private val isConsumed = AtomicBoolean(false)

    internal fun getValue(): T? =
        if (isConsumed.compareAndSet(false, true)) payload
        else null
}

fun <T> T.toOneTimeEvent() = OneTimeEvent(this)

/**
 * Consume payload only once
 */
fun <T> OneTimeEvent<T>?.consumeOnce(block: (T) -> Unit){
    this?.getValue()?.let { block(it) }
}