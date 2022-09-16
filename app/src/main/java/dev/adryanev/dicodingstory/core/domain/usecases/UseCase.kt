package dev.adryanev.dicodingstory.core.domain.usecases

import arrow.core.Either
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import kotlinx.coroutines.flow.Flow

abstract class UseCase<ResultType, Params> {

    abstract suspend operator fun invoke(params: Params): Flow<Either<Failure, ResultType>>
}

object NoParams