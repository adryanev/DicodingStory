package dev.adryanev.dicodingstory.core.networks.middlewares.providers

import dev.adryanev.dicodingstory.core.networks.middlewares.NetworkMiddleware

class MiddlewareProviderImpl private constructor(
    private val middlewares: List<NetworkMiddleware> = listOf()
) : MiddlewareProvider {
    class Builder(
        private val middlewareList: MutableList<NetworkMiddleware> = mutableListOf()
    ) {
        fun addMiddleware(networkMiddleware: NetworkMiddleware) = apply {
            this.middlewareList.add(networkMiddleware)
        }

        fun build() = MiddlewareProviderImpl(middlewares = middlewareList)
    }

    override fun getAll(): List<NetworkMiddleware> = middlewares
}