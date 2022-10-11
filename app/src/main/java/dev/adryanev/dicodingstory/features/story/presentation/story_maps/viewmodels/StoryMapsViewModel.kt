package dev.adryanev.dicodingstory.features.story.presentation.story_maps.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.Option
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.adryanev.dicodingstory.core.domain.usecases.NoParams
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewModel
import dev.adryanev.dicodingstory.features.story.domain.usecases.GetLatestStoryWithLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class StoryMapsViewModel @Inject constructor(
    private val getLatestStoryWithLocation: GetLatestStoryWithLocation
) : ViewModel(), MviViewModel<StoryMapsState> {
    private val _state = MutableStateFlow(StoryMapsState.initial())
    override val state: LiveData<StoryMapsState>
        get() = _state.asLiveData()

    init {
        loadStoryWithLocation()
    }
    @VisibleForTesting
    private fun loadStoryWithLocation() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            getLatestStoryWithLocation(NoParams).collectLatest { either ->

                _state.update { it.copy(storyList = Option.fromNullable(either)) }
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}