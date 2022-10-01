package dev.adryanev.dicodingstory.services.locations.domain.repositories

import arrow.core.Either
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.services.locations.domain.entities.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getCurrentLocation(): Flow<Either<Failure, Location>>
}