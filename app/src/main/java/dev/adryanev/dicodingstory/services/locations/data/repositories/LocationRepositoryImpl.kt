package dev.adryanev.dicodingstory.services.locations.data.repositories

import arrow.core.Either
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.domain.failures.LocationFailure
import dev.adryanev.dicodingstory.services.locations.data.datasources.remote.GoogleLocationDataSource
import dev.adryanev.dicodingstory.services.locations.data.models.LocationModel
import dev.adryanev.dicodingstory.services.locations.data.models.toDomain
import dev.adryanev.dicodingstory.services.locations.domain.entities.Location
import dev.adryanev.dicodingstory.services.locations.domain.repositories.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val googleLocationDataSource: GoogleLocationDataSource
): LocationRepository {
    override suspend fun getCurrentLocation(): Flow<Either<Failure, Location>> {
        return flow {
            try {
                googleLocationDataSource.locationFlow().collect {
                    val locationModel = LocationModel(
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                    emit(Either.Right(locationModel.toDomain()))
                }

            } catch (exception: Exception){
                Timber.e(exception)
                emit(Either.Left(LocationFailure(exception.message?: "")))
            }

        }
    }
}