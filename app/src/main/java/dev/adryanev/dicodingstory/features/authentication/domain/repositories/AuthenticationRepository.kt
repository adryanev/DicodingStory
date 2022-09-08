package dev.adryanev.dicodingstory.features.authentication.domain.repositories

import dev.adryanev.dicodingstory.features.authentication.domain.entities.LoginForm
import dev.adryanev.dicodingstory.features.authentication.domain.entities.RegisterForm
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User
import dev.adryanev.functional_programming.Either
import dev.adryanev.functional_programming.Failure

interface AuthenticationRepository {

    suspend fun login(loginForm: LoginForm) : Either<Failure, User>
    suspend fun register(registerForm: RegisterForm): Either<Failure, Unit>
}