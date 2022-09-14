package dev.adryanev.dicodingstory.core.domain.usecases

import dev.adryanev.functional_programming.Either
import dev.adryanev.functional_programming.Failure
import kotlinx.coroutines.flow.Flow

abstract class UseCase<ResultType, Params> {

    abstract suspend operator fun invoke(params: Params): Flow<Either<Failure, ResultType>>
}

class NoParams