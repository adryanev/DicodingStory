package dev.adryanev.dicodingstory.core.presentations.mvi

import androidx.lifecycle.LiveData

/**
 * @param S Top class of the [MviViewState] the [MviViewModel] will be emitting.
 */
interface MviViewModel<S : MviViewState> {
    val state: LiveData<S>
}