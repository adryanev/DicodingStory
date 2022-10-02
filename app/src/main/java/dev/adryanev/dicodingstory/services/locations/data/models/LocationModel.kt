package dev.adryanev.dicodingstory.services.locations.data.models

import dev.adryanev.dicodingstory.services.locations.domain.entities.Location

data class LocationModel(
    val latitude: Double,
    val longitude: Double
)

fun LocationModel.toDomain() = Location(
    latitude = latitude,
    longitude = longitude
)