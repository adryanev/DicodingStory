package dev.adryanev.dicodingstory.features.authentication.data.models.login

import dev.adryanev.dicodingstory.features.authentication.domain.entities.User

data class LoginResponse(
    val error: Boolean?,
    val message: String?,
    val loginResult: LoginResult?,
)

data class LoginResult(
    val userId: String?,
    val name: String?,
    val token: String?,
)

fun LoginResult.toDomain() =
    User(userId = this.userId.orEmpty(), name = this.name.orEmpty(), token = this.token.orEmpty())