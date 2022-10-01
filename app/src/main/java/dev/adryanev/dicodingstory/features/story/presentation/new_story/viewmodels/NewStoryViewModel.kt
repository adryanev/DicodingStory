package dev.adryanev.dicodingstory.features.story.presentation.new_story.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.Option
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.adryanev.dicodingstory.core.domain.usecases.NoParams
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewModel
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.GetLoggedInUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewStoryViewModel @Inject constructor(
    private val getLoggedInUser: GetLoggedInUser,
) : ViewModel(), MviViewModel<NewStoryState> {

    private val _state = MutableStateFlow(NewStoryState.initial())
    override val state: LiveData<NewStoryState>
        get() = _state.asLiveData()

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            getLoggedInUser(NoParams).collectLatest {
                _state.value = _state.value.copy(
                    getLoggedInUser = Option.fromNullable(it)
                )
            }
        }
    }

    fun setUser(user: User?) {
        _state.value = _state.value.copy(loggedInUser = user)
    }

    fun descriptionChanged(description: String) {
        _state.value = _state.value.copy(
            description = description
        )

    }


}