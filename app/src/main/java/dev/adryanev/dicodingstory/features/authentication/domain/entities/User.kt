package dev.adryanev.dicodingstory.features.authentication.domain.entities

data class User(
    val userId: String,
    val name: String,
    val token: String,
)