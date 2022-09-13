package dev.adryanev.dicodingstory.features.authentication.data.datasources.networks

import com.squareup.moshi.JsonAdapter
import dev.adryanev.dicodingstory.core.di.annotations.IoDispatcher
import dev.adryanev.dicodingstory.core.networks.middlewares.extensions.safeCall
import dev.adryanev.dicodingstory.core.networks.middlewares.providers.MiddlewareProvider
import dev.adryanev.dicodingstory.core.networks.models.ErrorResponse
import dev.adryanev.dicodingstory.features.authentication.data.datasources.networks.services.AuthenticationService
import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginPayload
import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginResponse
import dev.adryanev.dicodingstory.features.authentication.data.models.register.RegisterPayload
import dev.adryanev.dicodingstory.features.authentication.data.models.register.RegisterResponse
import dev.adryanev.functional_programming.Either
import dev.adryanev.functional_programming.Failure
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AuthenticationRemoteDataSourceImpl @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val middlewareProvider: MiddlewareProvider,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val adapter: JsonAdapter<ErrorResponse>,
) : AuthenticationRemoteDataSource {
    override suspend fun registerUser(registerPayload: RegisterPayload): Either<Failure, RegisterResponse> {
        return safeCall(
            middlewares = middlewareProvider.getAll(),
            ioDispatcher = ioDispatcher,
            adapter = adapter,
            retrofitCall = {
                authenticationService.registerUser(registerPayload)
            }
        )
    }

    override suspend fun loginUser(loginPayload: LoginPayload): Either<Failure, LoginResponse> {
        return safeCall(
            middlewares = middlewareProvider.getAll(),
            ioDispatcher = ioDispatcher,
            adapter = adapter,
            retrofitCall = {
                authenticationService.loginUser(loginPayload = loginPayload)
            }
        )
    }
}