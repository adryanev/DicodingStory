package dev.adryanev.dicodingstory.features.story.presentation.story_list.viewmodels

import androidx.paging.PagingData
import arrow.core.Option
import arrow.core.none
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewState
import dev.adryanev.dicodingstory.features.story.domain.entities.Story

data class StoryListState(
    val isLoading: Boolean,
    val isRefresh: Boolean,
    val storyList: Option<PagingData<Story>>,
) : MviViewState {
    companion object {
        fun initial() = StoryListState(
            isLoading = false,
            isRefresh = false,
            storyList = none(),
        )
    }
}
