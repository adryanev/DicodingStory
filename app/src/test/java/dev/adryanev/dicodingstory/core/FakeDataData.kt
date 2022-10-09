package dev.adryanev.dicodingstory.core

import android.location.Location
import arrow.core.Either
import arrow.core.right
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginPayload
import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginResponse
import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginResult
import dev.adryanev.dicodingstory.features.authentication.data.models.register.RegisterPayload
import dev.adryanev.dicodingstory.features.authentication.data.models.register.RegisterResponse
import dev.adryanev.dicodingstory.features.story.data.models.CreateStoryPayload
import dev.adryanev.dicodingstory.features.story.data.models.CreateStoryResponse
import dev.adryanev.dicodingstory.features.story.data.models.StoryListResponse
import dev.adryanev.dicodingstory.features.story.data.models.StoryResponse
import kotlinx.coroutines.flow.flow
import java.time.ZonedDateTime


fun createLoginResponseSuccess(): Either<Failure, LoginResponse> =
    createLoginResponse().right()


fun createLoginResponse() =
    LoginResponse(error = false, message = "Login Success", loginResult = createLoginResult())

fun createLoginResult() = LoginResult(userId = "user-12919931", name = "Yo mamen", token = "secret")

fun createLoginPayload() = LoginPayload(email = "email@example.com", password = "12345678")

fun createRegisterResponseSuccess(): Either<Failure, RegisterResponse> =
    createRegisterResponse().right()

fun createRegisterResponse() = RegisterResponse(error = false, message = "Success Register User")
fun createRegisterPayload() =
    RegisterPayload(name = "Yo mamen", email = "email@example.com", password = "12345678")

fun createLoggedInResponse(): Either<Failure, LoginResult> = createLoginResult().right()

fun createLocationFlow() = flow {
    emit(createLocationResponse())
}

fun createLocationResponse(): Location {
    val mockLocation = Location("provider")
    mockLocation.latitude = 0.10301
    mockLocation.longitude = 131.241
    return mockLocation

}

fun createStoryDataResponse() = StoryResponse(
    error = false,
    message = "story created",
    listStory = createStoryListResponse()
)

fun createStoryListResponse() = listOf(
    StoryListResponse(
        id = "121",
        name = "Hello",
        description = "Nothing",
        photoUrl = "https://google.com",
        createdAt = ZonedDateTime.now().toString(),
        latitude = 0.10301,
        longitude = 131.241

    )
)

fun createAddStoryPayload() = CreateStoryPayload(
    "Description", 0.10301, 131.241
)

fun createAddStoryResponse() = CreateStoryResponse(error = false, message = "Create story success")