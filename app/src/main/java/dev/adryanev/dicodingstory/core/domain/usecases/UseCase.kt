package dev.adryanev.dicodingstory.core.domain.usecases

import dev.adryanev.functional_programming.Either
import dev.adryanev.functional_programming.Failure

abstract class UseCase<ResultType, Params> {

   abstract suspend fun invoke(params :Params) : Either<Failure, ResultType>
}

class NoParams