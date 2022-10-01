package dev.adryanev.dicodingstory.features.story.presentation.story_list.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Option
import arrow.core.none
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.adryanev.dicodingstory.core.domain.usecases.NoParams
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewModel
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.LogoutUser
import dev.adryanev.dicodingstory.features.story.domain.usecases.GetLatestStory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryListViewModel @Inject constructor(
    private val getLatestStory: GetLatestStory, private val logoutUser: LogoutUser
) : ViewModel(), MviViewModel<StoryListState> {

    private val _state = MutableStateFlow(StoryListState.initial())
    override val state: StateFlow<StoryListState>
        get() = _state

    init {
        getLatestStory()
    }

    fun getLatestStory() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            getLatestStory(NoParams).collectLatest {
                _state.value = _state.value.copy(
                    storyList = Option.fromNullable(it)
                )
                _state.value = _state.value.copy(
                    isLoading = false
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUser(NoParams).collectLatest {
                _state.value = _state.value.copy(
                    logout = Option.fromNullable(it)
                )
                _state.value = _state.value.copy(
                    logout = none()
                )
            }
        }
    }
}