package dev.adryanev.dicodingstory.features.story.presentation.story_maps.viewmodels

import arrow.core.Either
import arrow.core.Option
import arrow.core.none
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewState
import dev.adryanev.dicodingstory.features.story.domain.entities.Story

data class StoryMapsState(
    val isLoading: Boolean,
    val storyList: Option<Either<Failure, List<Story>>>
) : MviViewState {
    companion object {
        fun initial() = StoryMapsState(
            isLoading = false,
            storyList = none()
        )
    }
}
