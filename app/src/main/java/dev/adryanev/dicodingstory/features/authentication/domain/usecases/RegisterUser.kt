package dev.adryanev.dicodingstory.features.authentication.domain.usecases

import arrow.core.Either
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.domain.usecases.UseCase
import dev.adryanev.dicodingstory.features.authentication.domain.entities.RegisterForm
import dev.adryanev.dicodingstory.features.authentication.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUser @Inject constructor(
    private val repository: AuthenticationRepository
) : UseCase<Unit, RegisterUserParams>() {
    override suspend fun invoke(params: RegisterUserParams): Flow<Either<Failure, Unit>> =
        repository.register(registerForm = params.registerForm)
}

data class RegisterUserParams(
    val registerForm: RegisterForm
)