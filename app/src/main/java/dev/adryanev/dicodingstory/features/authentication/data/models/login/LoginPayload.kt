package dev.adryanev.dicodingstory.features.authentication.data.models.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.adryanev.dicodingstory.features.authentication.domain.entities.LoginForm

@JsonClass(generateAdapter = true)
data class LoginPayload(
    @field:Json(name = "email") val email: String,
    @field:Json(name = "password") val password: String,
) {
    companion object {
        fun fromDomain(loginForm: LoginForm) = LoginPayload(
            email = loginForm.emailAddress.value(),
            password = loginForm.password.value()
        )
    }
}