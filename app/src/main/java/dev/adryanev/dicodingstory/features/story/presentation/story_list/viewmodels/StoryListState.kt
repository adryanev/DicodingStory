package dev.adryanev.dicodingstory.features.story.presentation.story_list.viewmodels

import arrow.core.Either
import arrow.core.Option
import arrow.core.none
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewState
import dev.adryanev.dicodingstory.features.story.domain.entities.Story

data class StoryListState(
    val isLoading: Boolean,
    val isRefresh: Boolean,
    val storyList: Option<Either<Failure, List<Story>>>,
    val logout: Option<Either<Failure, Unit>>
) : MviViewState {
    companion object {
        fun initial() = StoryListState(
            isLoading = false,
            isRefresh = false,
            storyList = none(),
            logout = none()
        )
    }
}
