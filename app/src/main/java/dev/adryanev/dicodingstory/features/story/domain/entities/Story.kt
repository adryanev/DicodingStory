package dev.adryanev.dicodingstory.features.story.domain.entities

import java.time.ZonedDateTime


data class Story(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String?,
    val createdAt: ZonedDateTime,
    val latitude: Double?,
    val longitude: Double?
)
