package dev.adryanev.dicodingstory.features.authentication.data.models.register

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterResponse(
    @field:Json(name = "error") val error: Boolean?,
    @field:Json(name = "message") val message: String?
)