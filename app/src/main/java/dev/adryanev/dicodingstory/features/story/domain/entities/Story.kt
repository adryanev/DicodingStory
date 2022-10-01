package dev.adryanev.dicodingstory.features.story.domain.entities

import android.os.Parcelable
import dev.adryanev.dicodingstory.services.locations.domain.entities.Location
import kotlinx.parcelize.Parcelize
import java.time.ZonedDateTime

@Parcelize
data class Story(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String?,
    val createdAt: ZonedDateTime,
   val location: Location?
): Parcelable
