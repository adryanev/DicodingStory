package dev.adryanev.dicodingstory.core.presentations.mvi

import kotlinx.coroutines.flow.Flow

interface MviProcessorHolder<A : MviAction, R : MviResult> {

    fun processAction(action: A): Flow<R>
}