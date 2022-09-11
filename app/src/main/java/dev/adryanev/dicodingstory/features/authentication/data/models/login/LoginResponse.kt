package dev.adryanev.dicodingstory.features.authentication.data.models.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @field:Json(name = "error") val error: Boolean?,
    @field:Json(name = "message") val message: String?,
    @field:Json(name = "loginResult") val loginResult: LoginResult?,
)

@JsonClass(generateAdapter = true)
data class LoginResult(
    @field:Json(name = "userId") val userId: String?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "token") val token: String?,
)

fun LoginResult.toDomain() =
    User(userId = this.userId.orEmpty(), name = this.name.orEmpty(), token = this.token.orEmpty())