package dev.adryanev.dicodingstory.core.networks.middlewares.providers

import dev.adryanev.dicodingstory.core.networks.middlewares.NetworkMiddleware

interface MiddlewareProvider {
    fun getAll(): List<NetworkMiddleware>
}