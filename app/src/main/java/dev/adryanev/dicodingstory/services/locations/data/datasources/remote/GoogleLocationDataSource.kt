package dev.adryanev.dicodingstory.services.locations.data.datasources.remote

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleLocationDataSource @Inject constructor(
    context: Context
) {
    companion object {
        private const val LOCATION_REQUEST_INTERVAL = 10000L
        private const val LOCATION_REQUEST_FASTEST_INTERVAL = 5000L
    }

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun locationFlow(): Flow<Location> = callbackFlow {
        val locationRequest = LocationRequest.create().apply {
            interval = LOCATION_REQUEST_INTERVAL
            fastestInterval = LOCATION_REQUEST_FASTEST_INTERVAL
            //priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
            // Use this option whenever uses the emulator, that's the only way to force the emulator
            // GPS to be activated
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                val location = result.lastLocation ?: return
                try {
                    this@callbackFlow.trySend(location).isSuccess
                    fusedLocationClient.removeLocationUpdates(this)

                } catch (e: Exception) {
                    Timber.e(e)
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        ).addOnFailureListener {
            close(it)
        }
        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }


}