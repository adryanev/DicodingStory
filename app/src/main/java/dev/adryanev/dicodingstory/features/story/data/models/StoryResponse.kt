package dev.adryanev.dicodingstory.features.story.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.adryanev.dicodingstory.features.story.domain.entities.Story
import dev.adryanev.dicodingstory.services.locations.domain.entities.Location
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
data class StoryResponse(
    @field:Json(name = "error") val error: Boolean?,
    @field:Json(name = "message") val message: String?,
    @field:Json(name = "listStory") val listStory: List<StoryListResponse>?
)

@JsonClass(generateAdapter = true)
data class StoryListResponse(
    @field:Json(name = "id") val id: String?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "photoUrl") val photoUrl: String?,
    @field:Json(name = "createdAt") val createdAt: String?,
    @field:Json(name = "lat") val latitude: Double?,
    @field:Json(name = "lon") val longitude: Double?
)

fun StoryListResponse.toDomain() = Story(
    id = id ?: "0",
    name = name ?: "",
    description = description ?: "",
    photoUrl = photoUrl,
    createdAt = ZonedDateTime.parse(createdAt),
    location = if ((latitude == null) || (longitude == null)) null else Location(
        latitude = latitude,
        longitude = longitude
    )

)
