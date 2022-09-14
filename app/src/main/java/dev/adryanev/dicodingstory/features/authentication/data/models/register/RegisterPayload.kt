package dev.adryanev.dicodingstory.features.authentication.data.models.register

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.adryanev.dicodingstory.features.authentication.domain.entities.RegisterForm

@JsonClass(generateAdapter = true)
data class RegisterPayload(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "email") val email: String,
    @field:Json(name = "password") val password: String,
) {
    companion object {
        fun fromDomain(registerForm: RegisterForm): RegisterPayload  =
            RegisterPayload(
                name = registerForm.name,
                email = registerForm.emailAddress.value(),
                password = registerForm.password.value()
            )
    }
}