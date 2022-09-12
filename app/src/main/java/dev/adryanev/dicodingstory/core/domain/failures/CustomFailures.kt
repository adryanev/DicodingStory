package dev.adryanev.dicodingstory.core.domain.failures

import dev.adryanev.functional_programming.Failure

class NetworkFailure(val message: String, val code: Int) : Failure.CustomFailure()
class NetworkMiddlewareFailure(val message: String) : Failure.CustomFailure()
class DatabaseFailure(val message: String): Failure.CustomFailure()
object SharedPreferenceFailure: Failure.CustomFailure()
object ServiceFailure: Failure.CustomFailure()
