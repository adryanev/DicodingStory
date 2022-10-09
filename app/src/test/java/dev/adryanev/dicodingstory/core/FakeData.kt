package dev.adryanev.dicodingstory.core

import androidx.paging.PagingData
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.domain.failures.NetworkFailure
import dev.adryanev.dicodingstory.core.domain.failures.SharedPreferenceFailure
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User
import dev.adryanev.dicodingstory.features.story.domain.entities.Story
import dev.adryanev.dicodingstory.services.locations.domain.entities.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.time.ZonedDateTime

fun createStoryPagingData() = flow {
    emit(PagingData.from(createStory()))
}

fun createStoryDataSuccess() = flow<Either<Failure, List<Story>>> {
    emit(Either.Right(createStory()))
}

fun createStoryDataFailure() = flow<Either<Failure, List<Story>>> {
    emit(Either.Left(NetworkFailure(message = "Not found", code = 404)))
}

fun createStory(): List<Story> = listOf(
    Story(
        id = "121",
        name = "Hello",
        description = "Nothing",
        photoUrl = "https://google.com",
        createdAt = ZonedDateTime.now(),
        location = Location(latitude = 0.10301, longitude = 131.241)
    )
)

fun createUserFound() = flow<Either<Failure, User?>> {
    emit(Either.Right(createUser()))
}

fun createUserNotFound() = flow<Either<Failure, User?>> {
    emit(Either.Right(null))
}

fun createLoginSuccess() = flow<Either<Failure, User>> {
    emit(Either.Right(createUser()))
}

fun createLoginFailed() = flow<Either<Failure, User>> {
    emit(Either.Left(NetworkFailure(message = "User Not Found", code = 400)))
}

fun createUser(): User = User(userId = "id", name = "user", token = "token")

fun createRegisterSuccess() = flow<Either<Failure, Unit>> {
    emit(Unit.right())
}

fun createRegisterFailure() = flow<Either<Failure, Unit>> {
    emit(NetworkFailure(message = "User Already Exist", code = 400).left())
}

fun createLogoutSuccess() = flow<Either<Failure, Unit>> {
    emit(Unit.right())
}

fun createLogoutFailure() = flow<Either<Failure, Unit>> {
    emit(SharedPreferenceFailure("Cannot logoutUser").left())
}

fun createStorySuccess() = flow<Either<Failure, Unit>> {
    emit(Unit.right())
}

fun createLocationSuccess() = flow<Either<Failure, Location>> {
    emit(createLocation().right())
}

fun createLocation() = Location(0.131214, 131.4113)

suspend fun createFile() = withContext(Dispatchers.IO) {
    File.createTempFile("test", "test")
}
