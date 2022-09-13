package dev.adryanev.dicodingstory.features.authentication.data.datasources.networks

import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginPayload
import dev.adryanev.dicodingstory.features.authentication.data.models.register.RegisterPayload

interface AuthenticationRemoteDataSource {
    suspend fun registerUser(registerPayload: RegisterPayload)
    suspend fun loginUser(loginPayload: LoginPayload)
}