package dev.adryanev.dicodingstory.services.locations.domain.usecases

import arrow.core.Either
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.domain.usecases.NoParams
import dev.adryanev.dicodingstory.core.domain.usecases.UseCase
import dev.adryanev.dicodingstory.services.locations.domain.entities.Location
import dev.adryanev.dicodingstory.services.locations.domain.repositories.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentLocation @Inject constructor(
    private val locationRepository: LocationRepository
) : UseCase<Location, NoParams>() {
    override suspend fun invoke(params: NoParams): Flow<Either<Failure, Location>> {
        return locationRepository.getCurrentLocation()
    }
}