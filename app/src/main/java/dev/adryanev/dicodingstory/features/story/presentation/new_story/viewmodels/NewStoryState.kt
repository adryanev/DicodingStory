package dev.adryanev.dicodingstory.features.story.presentation.new_story.viewmodels

import arrow.core.Either
import arrow.core.Option
import arrow.core.none
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.presentations.mvi.MviViewState
import dev.adryanev.dicodingstory.core.utils.OneTimeEvent
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User
import dev.adryanev.dicodingstory.services.locations.domain.entities.Location
import java.io.File

data class NewStoryState(
    val isLoading: Boolean,
    val description: String?,
    val getLoggedInUser: Option<Either<Failure, User?>>,
    val getUserLocation: Option<Either<Failure, Location>>,
    val loggedInUser: User?,
    val storyPicture: File?,
    val errorMessage: OneTimeEvent<String>?,
    val createNewStory: Option<Either<Failure, Unit>>,
    val userLocation: Location?

) : MviViewState {
    companion object {
        fun initial() = NewStoryState(
            isLoading = false,
            description = null,
            getLoggedInUser = none(),
            getUserLocation = none(),
            loggedInUser = null,
            storyPicture = null,
            errorMessage = null,
            createNewStory = none(),
            userLocation = null
        )
    }
}
