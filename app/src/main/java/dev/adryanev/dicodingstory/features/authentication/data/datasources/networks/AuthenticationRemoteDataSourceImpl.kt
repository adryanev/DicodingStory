package dev.adryanev.dicodingstory.features.authentication.data.datasources.networks

import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginPayload
import dev.adryanev.dicodingstory.features.authentication.data.models.register.RegisterPayload

class AuthenticationRemoteDataSourceImpl : AuthenticationRemoteDataSource {
    override suspend fun registerUser(registerPayload: RegisterPayload) {
        TODO("Not yet implemented")
    }

    override suspend fun loginUser(loginPayload: LoginPayload) {
        TODO("Not yet implemented")
    }
}