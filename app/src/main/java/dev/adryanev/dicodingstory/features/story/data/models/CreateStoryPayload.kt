package dev.adryanev.dicodingstory.features.story.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateStoryPayload(
    @field:Json(name = "description") val description: String?,
    val latitude: Double?,
    val longitude: Double?
)