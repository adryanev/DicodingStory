package dev.adryanev.dicodingstory.features.authentication.data.datasources.networks

import arrow.core.Either
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginPayload
import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginResponse
import dev.adryanev.dicodingstory.features.authentication.data.models.register.RegisterPayload
import dev.adryanev.dicodingstory.features.authentication.data.models.register.RegisterResponse


interface AuthenticationRemoteDataSource {
    suspend fun registerUser(registerPayload: RegisterPayload): Either<Failure, RegisterResponse>
    suspend fun loginUser(loginPayload: LoginPayload): Either<Failure, LoginResponse>
}