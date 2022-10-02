package dev.adryanev.dicodingstory.features.story.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.adryanev.dicodingstory.features.story.domain.entities.StoryForm

@JsonClass(generateAdapter = true)
data class CreateStoryPayload(
    @field:Json(name = "description") val description: String?,
    val latitude: Double?,
    val longitude: Double?
) {
    companion object {
        fun fromDomain(form: StoryForm) = CreateStoryPayload(
            description = form.description,
            latitude = form.location?.latitude,
            longitude = form.location?.longitude
        )
    }
}