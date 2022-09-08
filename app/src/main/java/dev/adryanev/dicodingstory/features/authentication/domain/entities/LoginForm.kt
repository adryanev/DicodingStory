package dev.adryanev.dicodingstory.features.authentication.domain.entities

import dev.adryanev.dicodingstory.shared.domain.value_object.EmailAddress
import dev.adryanev.dicodingstory.shared.domain.value_object.Password

data class LoginForm(
    val emailAddress: EmailAddress,
    val password: Password
)