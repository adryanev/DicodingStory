package dev.adryanev.dicodingstory.features.story.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.adryanev.dicodingstory.core.utils.DatabaseConstants
import dev.adryanev.dicodingstory.features.story.domain.entities.Story
import dev.adryanev.dicodingstory.services.locations.domain.entities.Location
import java.time.ZonedDateTime

@Entity(tableName = DatabaseConstants.STORY_TABLE)
data class StoryLocalModel(
    @PrimaryKey
    val id: String,
    val name: String?,
    val description: String?,
    val photoUrl: String?,
    val createdAt: String?,
    val latitude: Double?,
    val longitude: Double?
) {
    companion object {
        fun fromResponse(response: StoryListResponse) = StoryLocalModel(
            id = response.id ?: "1",
            name = response.name,
            description = response.description,
            photoUrl = response.photoUrl,
            createdAt = response.createdAt,
            latitude = response.latitude,
            longitude = response.longitude
        )
    }
}


fun StoryLocalModel.toDomain() = Story(
    id = id,
    name = name ?: "",
    description = description ?: "",
    photoUrl = photoUrl,
    createdAt = ZonedDateTime.parse(createdAt),
    location = if ((latitude == null) || (longitude == null)) null else Location(
        latitude = latitude,
        longitude = longitude
    )

)

@Entity(tableName = DatabaseConstants.STORY_REMOTE_KEY)
data class StoryRemoteKey(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)


