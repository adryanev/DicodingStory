package dev.adryanev.dicodingstory.core.networks.middlewares

import dev.adryanev.dicodingstory.R
import dev.adryanev.dicodingstory.core.domain.failures.NetworkMiddlewareFailure
import dev.adryanev.dicodingstory.core.utils.connectivity.ConnectivityUtils
import dev.adryanev.dicodingstory.core.utils.resource.ResourceProvider
import javax.inject.Inject

class ConnectivityMiddleware @Inject constructor(
    private val connectivityUtils: ConnectivityUtils,
    private val resourceProvider: ResourceProvider,
) : NetworkMiddleware() {
    override val failure: NetworkMiddlewareFailure
        get() = NetworkMiddlewareFailure(
            message = resourceProvider.getString(R.string.no_internet_connection)
        )

    override fun isValid(): Boolean = connectivityUtils.isNetworkAvailable()
}