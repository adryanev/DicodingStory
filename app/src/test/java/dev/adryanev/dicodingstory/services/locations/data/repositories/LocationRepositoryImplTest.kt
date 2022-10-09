package dev.adryanev.dicodingstory.services.locations.data.repositories

import app.cash.turbine.test
import arrow.core.getOrElse
import dev.adryanev.dicodingstory.core.createLocation
import dev.adryanev.dicodingstory.core.createLocationFlow
import dev.adryanev.dicodingstory.core.data.repositories.BaseRepositoryTest
import dev.adryanev.dicodingstory.core.utils.mock
import dev.adryanev.dicodingstory.services.locations.data.datasources.remote.GoogleLocationDataSource
import dev.adryanev.dicodingstory.services.locations.domain.repositories.LocationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import kotlin.time.Duration

@ExperimentalCoroutinesApi
class LocationRepositoryImplTest : BaseRepositoryTest() {

    private lateinit var systemUnderTest: LocationRepository
    private val googleLocationDataSource = mock<GoogleLocationDataSource>()

    @Before
    fun setUp() {
        systemUnderTest = LocationRepositoryImpl(googleLocationDataSource)
    }

    @Test
    fun getCurrentLocation() {
        testCoroutineRule.runTest {
            `when`(googleLocationDataSource.locationFlow()).thenReturn(
                createLocationFlow()
            )
            launchTest {
                val result = systemUnderTest.getCurrentLocation()
                verify(googleLocationDataSource).locationFlow()

                result.test(Duration.ZERO) {
                    val data = expectMostRecentItem()
                    assertTrue(data.isRight())
                    val location = data.getOrElse { null }
                    assertNotNull(location)
                    assertEquals(location!!, createLocation())
                    expectNoEvents()
                }
            }
        }
    }
}