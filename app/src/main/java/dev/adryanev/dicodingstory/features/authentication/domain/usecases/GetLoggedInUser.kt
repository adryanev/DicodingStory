package dev.adryanev.dicodingstory.features.authentication.domain.usecases

import arrow.core.Either
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.domain.usecases.NoParams
import dev.adryanev.dicodingstory.core.domain.usecases.UseCase
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User
import dev.adryanev.dicodingstory.features.authentication.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLoggedInUser @Inject constructor(
    private val repository: AuthenticationRepository
) : UseCase<User?, NoParams>() {
    override suspend fun invoke(params: NoParams): Flow<Either<Failure, User?>> =
        repository.getLoggedInUser()
}

