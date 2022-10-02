package dev.adryanev.dicodingstory.features.story.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateStoryResponse(
    @field:Json(name = "error") val error: Boolean?,
    @field:Json(name = "message" ) val message: String?
)
