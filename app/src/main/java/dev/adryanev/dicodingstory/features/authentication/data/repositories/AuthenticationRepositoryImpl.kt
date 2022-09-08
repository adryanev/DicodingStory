package dev.adryanev.dicodingstory.features.authentication.data.repositories

import dev.adryanev.dicodingstory.features.authentication.domain.entities.LoginForm
import dev.adryanev.dicodingstory.features.authentication.domain.entities.RegisterForm
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User
import dev.adryanev.dicodingstory.features.authentication.domain.repositories.AuthenticationRepository
import dev.adryanev.functional_programming.Either
import dev.adryanev.functional_programming.Failure

class AuthenticationRepositoryImpl : AuthenticationRepository{

    override suspend fun login(loginForm: LoginForm): Either<Failure, User> {
        TODO("Not yet implemented")
    }

    override suspend fun register(registerForm: RegisterForm): Either<Failure, Unit> {
        TODO("Not yet implemented")
    }
}