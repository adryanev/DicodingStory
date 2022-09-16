package dev.adryanev.dicodingstory.core.domain.failures


class NetworkFailure(val message: String, val code: Int) : Failure.CustomFailure()
class NetworkMiddlewareFailure(val message: String) : Failure.CustomFailure()
object TimeOutFailure : Failure.CustomFailure()
object SslFailure : Failure.CustomFailure()

class DatabaseFailure(val message: String) : Failure.CustomFailure()
class SharedPreferenceFailure(val message: String) : Failure.CustomFailure()
object ServiceFailure : Failure.CustomFailure()
