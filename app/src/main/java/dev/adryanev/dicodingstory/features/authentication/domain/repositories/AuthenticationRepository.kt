package dev.adryanev.dicodingstory.features.authentication.domain.repositories

import arrow.core.Either
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.features.authentication.domain.entities.LoginForm
import dev.adryanev.dicodingstory.features.authentication.domain.entities.RegisterForm
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    suspend fun login(loginForm: LoginForm): Flow<Either<Failure, User>>
    suspend fun register(registerForm: RegisterForm): Flow<Either<Failure, Unit>>
    suspend fun logout(): Flow<Either<Failure, Unit>>
    suspend fun getLoggedInUser(): Flow<Either<Failure, User?>>
}