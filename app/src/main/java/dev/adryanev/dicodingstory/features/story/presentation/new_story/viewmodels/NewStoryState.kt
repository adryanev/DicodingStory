package dev.adryanev.dicodingstory.features.story.presentation.new_story.viewmodels

import arrow.core.Either
import arrow.core.Option
import arrow.core.none
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewState
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User

data class NewStoryState(
    val isLoading: Boolean,
    val description: String?,
    val getLoggedInUser: Option<Either<Failure, User?>>,
    val loggedInUser: User?

) : MviViewState {
    companion object {
        fun initial() = NewStoryState(
            isLoading = false,
            description = null,
            getLoggedInUser = none(),
            loggedInUser = null
        )
    }
}
