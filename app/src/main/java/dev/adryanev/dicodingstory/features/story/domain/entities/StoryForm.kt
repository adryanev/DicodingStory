package dev.adryanev.dicodingstory.features.story.domain.entities

import dev.adryanev.dicodingstory.services.locations.domain.entities.Location
import java.io.File

data class StoryForm(
    val description: String,
    val location: Location?,
    val photo: File

)
