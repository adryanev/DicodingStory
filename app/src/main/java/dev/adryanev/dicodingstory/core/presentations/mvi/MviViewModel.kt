package dev.adryanev.dicodingstory.core.presentations.mvi

import kotlinx.coroutines.flow.StateFlow

/**
 * @param S Top class of the [MviViewState] the [MviViewModel] will be emitting.
 */
interface MviViewModel< S : MviViewState> {
    val state: StateFlow<S>
}