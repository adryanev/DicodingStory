package dev.adryanev.dicodingstory.features.authentication.presentation.login.viewmodel

import arrow.core.Either
import arrow.core.Option
import arrow.core.none
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewState
import dev.adryanev.dicodingstory.core.utils.OneTimeEvent
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User
import dev.adryanev.dicodingstory.shared.domain.value_object.EmailAddress
import dev.adryanev.dicodingstory.shared.domain.value_object.Password

data class LoginFormViewState(
    val emailAddress: EmailAddress?,
    val password: Password?,
    val isLoading: Boolean,
    val error: OneTimeEvent<Failure>?,
    val loginResult: Option<Either<Failure, User?>>
) : MviViewState {

    companion object {
        fun initial(): LoginFormViewState = LoginFormViewState(
            emailAddress = null,
            password = null,
            isLoading = false,
            error = null,
            loginResult = none()
        )
    }
}
