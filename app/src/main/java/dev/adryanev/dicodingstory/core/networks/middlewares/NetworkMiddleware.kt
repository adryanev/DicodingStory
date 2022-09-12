package dev.adryanev.dicodingstory.core.networks.middlewares

import dev.adryanev.dicodingstory.core.domain.failures.NetworkMiddlewareFailure

abstract class NetworkMiddleware {
    abstract val failure: NetworkMiddlewareFailure
    abstract fun isValid(): Boolean

}