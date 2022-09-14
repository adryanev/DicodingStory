package dev.adryanev.dicodingstory.core.networks.interceptors

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import dev.adryanev.dicodingstory.core.utils.PreferenceConstants
import dev.adryanev.dicodingstory.features.authentication.data.models.login.LoginResult
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preference: SharedPreferences,
    private val moshi: Moshi
) : Interceptor {

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
        val data = preference.getString(PreferenceConstants.USER, "")
        val adapter = moshi.adapter(LoginResult::class.java)
        val user = data?.let { adapter.fromJson(it) }
        return user?.token
    }
}