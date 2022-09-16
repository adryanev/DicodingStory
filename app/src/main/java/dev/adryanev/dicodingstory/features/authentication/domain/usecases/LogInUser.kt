package dev.adryanev.dicodingstory.features.authentication.domain.usecases

import arrow.core.Either
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.domain.usecases.UseCase
import dev.adryanev.dicodingstory.features.authentication.domain.entities.LoginForm
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User
import dev.adryanev.dicodingstory.features.authentication.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogInUser @Inject constructor(
    private val repository: AuthenticationRepository
) : UseCase<User, LogInUserParams>() {
    override suspend fun invoke(params: LogInUserParams): Flow<Either<Failure, User>> =
        repository.login(loginForm = params.loginForm)
}

data class LogInUserParams(
    val loginForm: LoginForm
)