package dev.adryanev.dicodingstory.core.presentations.mvi

/**
 * Object representing a UI that will
 * a) emit its intents to a view model,
 * b) subscribes to a view model for rendering its UI.
 *
 * @param S Top class of the [MviViewState] the [MviView] will be subscribing to.
 */
interface MviView<in S : MviViewState> {
    /**
     * Entry point for the [MviView] to render itself based on a [MviViewState].
     */
    fun render(state: S)
}