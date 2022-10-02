package dev.adryanev.dicodingstory.features.authentication.presentation.login.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.Option
import arrow.core.none
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewModel
import dev.adryanev.dicodingstory.features.authentication.domain.entities.LoginForm
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.LogInUser
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.LogInUserParams
import dev.adryanev.dicodingstory.shared.domain.value_object.EmailAddress
import dev.adryanev.dicodingstory.shared.domain.value_object.Password
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginFormViewModel @Inject constructor(
    private val logInUser: LogInUser,
) : ViewModel(), MviViewModel<LoginFormViewState> {
    private val _state = MutableStateFlow(LoginFormViewState.initial())

    override val state: LiveData<LoginFormViewState>
        get() = _state.asLiveData()

    fun emailAddressChanged(email: String) {
        if (email.isEmpty()) return
        val emailAddress = EmailAddress(email)
        _state.update { it.copy(emailAddress = emailAddress) }
    }

    fun passwordChanged(stringPassword: String) {
        val password = Password(stringPassword)
        _state.update {
            it.copy(password = password)
        }
    }

    fun loginButtonPressed() {
        val email = _state.value.emailAddress
        val password = _state.value.password
        if (email == null && password == null) {
            return
        }
        _state.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val result = logInUser(
                LogInUserParams(
                    LoginForm(emailAddress = email!!, password = password!!)
                )
            )
            result.collectLatest { either ->
                _state.update {
                    it.copy(loginResult = Option.fromNullable(either))
                }
                _state.update {
                    it.copy(
                        loginResult = none(),
                        isLoading = false
                    )
                }
            }
        }
    }
}