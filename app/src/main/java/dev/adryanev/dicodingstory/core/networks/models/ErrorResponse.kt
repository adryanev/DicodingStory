package dev.adryanev.dicodingstory.core.networks.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @field:Json(name = "error") val error: Boolean?,
    @field:Json(name = "message") val message: String?,
)
