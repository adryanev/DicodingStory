package dev.adryanev.dicodingstory.features.authentication.domain.repositories

import dev.adryanev.dicodingstory.features.authentication.domain.entities.LoginForm
import dev.adryanev.dicodingstory.features.authentication.domain.entities.RegisterForm
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User
import dev.adryanev.functional_programming.Either
import dev.adryanev.functional_programming.Failure
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    suspend fun login(loginForm: LoginForm) : Flow<Either<Failure, User>>
    suspend fun register(registerForm: RegisterForm): Flow<Either<Failure, Unit>>
}