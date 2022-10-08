package dev.adryanev.dicodingstory.features.story.presentation.story_home.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.Option
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.adryanev.dicodingstory.core.domain.usecases.NoParams
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewModel
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.LogoutUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryHomeViewModel @Inject constructor(
    private val logoutUser: LogoutUser,
) : ViewModel(), MviViewModel<StoryHomeState> {

    private val _state = MutableStateFlow(StoryHomeState.initial())
    override val state: LiveData<StoryHomeState>
        get() = _state.asLiveData()

    fun logout() {
        viewModelScope.launch {
            logoutUser(NoParams).collectLatest { either ->
                _state.update {
                    it.copy(logout = Option.fromNullable(either))
                }

            }
        }
    }
}