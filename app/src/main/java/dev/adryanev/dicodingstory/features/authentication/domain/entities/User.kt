package dev.adryanev.dicodingstory.features.authentication.domain.entities

import dev.adryanev.dicodingstory.shared.domain.value_object.EmailAddress

data class User(
    val userId: String,
    val name: String,
    val token: String,
)