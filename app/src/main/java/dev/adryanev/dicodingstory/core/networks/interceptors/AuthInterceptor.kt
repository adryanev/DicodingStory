package dev.adryanev.dicodingstory.core.networks.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Implements Auth Interceptor
        throw NotImplementedError()
    }
}