package dev.adryanev.dicodingstory.features.story.presentation.story_list.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.Option
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewModel
import dev.adryanev.dicodingstory.features.story.domain.usecases.GetLatestStory
import dev.adryanev.dicodingstory.features.story.domain.usecases.GetLatestStoryParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryListViewModel @Inject constructor(
    private val getLatestStory: GetLatestStory,
) : ViewModel(), MviViewModel<StoryListState> {

    private val _state = MutableStateFlow(StoryListState.initial())
    override val state: LiveData<StoryListState>
        get() = _state.asLiveData()

    init {
        latestStory()
    }

    private fun latestStory(page: Int? = 1) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            getLatestStory(GetLatestStoryParams(page)).collectLatest { either ->

                _state.update {
                    it.copy(storyList = Option.fromNullable(either))
                }
                _state.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun refreshPage() {

        viewModelScope.launch {

            _state.update {
                it.copy(isRefresh = true, isLoading = true)
            }
            getLatestStory(GetLatestStoryParams(1)).collectLatest { either ->

                _state.update {
                    it.copy(
                        storyList = Option.fromNullable(either),

                        )
                }

                _state.update {
                    it.copy(
                        isRefresh = false,
                        isLoading = false
                    )
                }


            }
        }


    }
}