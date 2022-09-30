package dev.adryanev.dicodingstory.features.authentication.data.datasources.remote.services

import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginPayload
import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginResponse
import dev.adryanev.dicodingstory.features.authentication.data.models.register.RegisterPayload
import dev.adryanev.dicodingstory.features.authentication.data.models.register.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationService {

    @POST("register")
    suspend fun registerUser(
        @Body registerPayload: RegisterPayload
    ): RegisterResponse

    @POST("login")
    suspend fun loginUser(
        @Body loginPayload: LoginPayload
    ): LoginResponse
}