package dev.adryanev.dicodingstory.features.splash.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Option
import arrow.core.none
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.adryanev.dicodingstory.core.domain.usecases.NoParams
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewModel
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.GetLoggedInUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getLoggedInUser: GetLoggedInUser
) : MviViewModel<SplashViewState>, ViewModel() {
    private val _state = MutableStateFlow(SplashViewState.initial())
    override val state: StateFlow<SplashViewState>
        get() = _state.asStateFlow()

    fun checkIsLoggedIn() {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            getLoggedInUser(NoParams).collectLatest {
                _state.value = _state.value.copy(
                    checkLoginOrFailure = Option.fromNullable(it)
                )
                _state.value = _state.value.copy(
                    isLoading = false,
                    checkLoginOrFailure = none()
                )
            }
        }

    }
}