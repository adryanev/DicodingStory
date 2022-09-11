package dev.adryanev.dicodingstory.core.networks.interceptors

import android.content.SharedPreferences
import dev.adryanev.dicodingstory.core.utils.PreferenceConstants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(private val preference: SharedPreferences) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val token = getUserToken()
        token?.let {
            val request: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            return chain.proceed(request)
        }
        return chain.proceed(chain.request())

    }

    private fun getUserToken(): String? {
        return preference.getString(PreferenceConstants.TOKEN_KEY, "")
    }
}