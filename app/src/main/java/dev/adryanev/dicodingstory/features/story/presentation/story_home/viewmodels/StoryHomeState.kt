package dev.adryanev.dicodingstory.features.story.presentation.story_home.viewmodels

import arrow.core.Either
import arrow.core.Option
import arrow.core.none
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewState

data class StoryHomeState(
    val logout: Option<Either<Failure, Unit>>
): MviViewState {
    companion object {
        fun initial() = StoryHomeState(logout = none())
    }
}
