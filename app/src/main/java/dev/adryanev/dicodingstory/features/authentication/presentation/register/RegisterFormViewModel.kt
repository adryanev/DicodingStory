package dev.adryanev.dicodingstory.features.authentication.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Option
import arrow.core.none
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewModel
import dev.adryanev.dicodingstory.features.authentication.domain.entities.RegisterForm
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.RegisterUser
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.RegisterUserParams
import dev.adryanev.dicodingstory.shared.domain.value_object.EmailAddress
import dev.adryanev.dicodingstory.shared.domain.value_object.Password
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterFormViewModel @Inject constructor(
    private val registerUser: RegisterUser,
) : ViewModel(), MviViewModel<RegisterFormViewState> {
    private val _state = MutableStateFlow(RegisterFormViewState.initial())

    override val state: StateFlow<RegisterFormViewState>
        get() = _state.asStateFlow()

    fun emailAddressChanged(email: String) {
        val emailAddress = EmailAddress(email)
        _state.value = _state.value.copy(emailAddress = emailAddress)
    }

    fun passwordChanged(stringPassword: String) {
        val password = Password(stringPassword)
        _state.value = _state.value.copy(password = password)
    }

    fun nameChanged(name: String) {
        _state.value = _state.value.copy(
            name = name,
        )
    }

    fun registerButtonPressed() {
        val name = _state.value.name
        val email = _state.value.emailAddress
        val password = _state.value.password

        if (name != null &&
            email != null &&
            password != null
        ) {
            return
        }

        _state.value = _state.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            val result = registerUser(
                RegisterUserParams(
                    registerForm = RegisterForm(
                        name= name!!,
                        emailAddress = email!!,
                        password = password!!
                    )
                )
            )

            result.collectLatest {
                _state.value = _state.value.copy(
                    registerResult = Option.fromNullable(it)
                )
            }

        }
        _state.value = _state.value.copy(
            isLoading = false,
            registerResult = none()
        )
    }
}