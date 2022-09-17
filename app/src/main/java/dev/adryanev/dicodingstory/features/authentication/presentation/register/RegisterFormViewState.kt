package dev.adryanev.dicodingstory.features.authentication.presentation.register

import arrow.core.Either
import arrow.core.Option
import arrow.core.none
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewState
import dev.adryanev.dicodingstory.shared.domain.value_object.EmailAddress
import dev.adryanev.dicodingstory.shared.domain.value_object.Password

data class RegisterFormViewState(
    val name: String?,
    val emailAddress: EmailAddress?,
    val password: Password?,
    val isLoading: Boolean,
    val registerResult: Option<Either<Failure, Unit>>
): MviViewState {
    companion object {
        fun initial(): RegisterFormViewState = RegisterFormViewState(
            name = null,
            emailAddress = null,
            password = null,
            isLoading = false,
            registerResult = none(),
        )
    }
}