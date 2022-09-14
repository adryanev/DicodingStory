package dev.adryanev.dicodingstory.features.authentication.data.repositories

import dev.adryanev.dicodingstory.core.di.annotations.IoDispatcher
import dev.adryanev.dicodingstory.features.authentication.data.datasources.local.AuthenticationLocalDataSource
import dev.adryanev.dicodingstory.features.authentication.data.datasources.networks.AuthenticationRemoteDataSource
import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginPayload
import dev.adryanev.dicodingstory.features.authentication.data.models.login.toDomain
import dev.adryanev.dicodingstory.features.authentication.data.models.register.RegisterPayload
import dev.adryanev.dicodingstory.features.authentication.domain.entities.LoginForm
import dev.adryanev.dicodingstory.features.authentication.domain.entities.RegisterForm
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User
import dev.adryanev.dicodingstory.features.authentication.domain.repositories.AuthenticationRepository
import dev.adryanev.functional_programming.Either
import dev.adryanev.functional_programming.Failure
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthenticationRepository {

    override suspend fun login(loginForm: LoginForm): Flow<Either<Failure, User>> {
        return flow {
            val result =
                authenticationRemoteDataSource.loginUser(LoginPayload.fromDomain(loginForm))
            val user = result.coMapSuccess { data -> data.loginResult?.toDomain()!! }

            if (!result.isError) {
                val save =
                    authenticationLocalDataSource
                        .saveLoginData(result.getSuccessOrNull()?.loginResult!!)
                if (save.isError) {
                    val err = save.getFailureOrNull()!!
                    emit(Either.Error(err))
                    return@flow
                }
            }
            emit(user)
        }.flowOn(ioDispatcher)
    }

    override suspend fun register(registerForm: RegisterForm): Flow<Either<Failure, Unit>> {
        return flow {
            emit(
                authenticationRemoteDataSource
                    .registerUser(RegisterPayload.fromDomain(registerForm))
                    .coMapSuccess { }
            )
        }.flowOn(ioDispatcher)
    }

    override suspend fun logout(): Flow<Either<Failure, Unit>> {
        return flow {
            emit(authenticationLocalDataSource.removeLoginData().coMapSuccess { })
        }.flowOn(ioDispatcher)
    }

    override suspend fun getLoggedInUser(): Flow<Either<Failure, User>> {
        return flow {
            val user = authenticationLocalDataSource.getLoginData().coMapSuccess {
                it.let { user -> user?.toDomain()!! }
            }
            emit(user)

        }.flowOn(ioDispatcher)
    }
}