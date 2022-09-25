package dev.adryanev.dicodingstory.features.splash.presentation.viewmodels

import arrow.core.Either
import arrow.core.Option
import arrow.core.none
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewState
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User

data class SplashViewState(
    val isLoading: Boolean,
    val checkLoginOrFailure: Option<Either<Failure, User?>>,
) : MviViewState{

    companion object {
        fun initial(): SplashViewState = SplashViewState(
            isLoading = false,
            checkLoginOrFailure = none())
    }
}

