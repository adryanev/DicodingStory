package dev.adryanev.dicodingstory.features.authentication.data.repositories

import app.cash.turbine.test
import arrow.core.getOrElse
import arrow.core.right
import dev.adryanev.dicodingstory.core.*
import dev.adryanev.dicodingstory.core.data.repositories.BaseRepositoryTest
import dev.adryanev.dicodingstory.core.utils.mock
import dev.adryanev.dicodingstory.features.authentication.data.datasources.local.AuthenticationLocalDataSource
import dev.adryanev.dicodingstory.features.authentication.data.datasources.remote.AuthenticationRemoteDataSource
import dev.adryanev.dicodingstory.features.authentication.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import kotlin.time.Duration

@ExperimentalCoroutinesApi
class AuthenticationRepositoryImplTest : BaseRepositoryTest() {

    private lateinit var systemUnderTest: AuthenticationRepository
    private val remoteSource = mock<AuthenticationRemoteDataSource>()
    private val localSource = mock<AuthenticationLocalDataSource>()

    @Before
    fun setUp() {
        systemUnderTest = AuthenticationRepositoryImpl(
            authenticationRemoteDataSource = remoteSource,
            authenticationLocalDataSource = localSource,
            testCoroutineRule.testCoroutineDispatcher
        )
    }

    @Test
    fun login() {
        testCoroutineRule.runTest {
            `when`(remoteSource.loginUser(createLoginPayload())).thenReturn(
                createLoginResponseSuccess()
            )
            `when`(localSource.saveLoginData(createLoginResult())).thenReturn(
                Unit.right()
            )
            launchTest {
                val result = systemUnderTest.login(createLoginForm())
                verify(remoteSource).loginUser(createLoginPayload())
                verify(localSource).saveLoginData(createLoginResult())
                result.test(timeout = Duration.ZERO) {
                    val data = expectMostRecentItem()

                    assertTrue(data.isRight())

                    val user = data.getOrElse { null }

                    assertNotNull(user)

                    assertEquals(user!!, createUser())

                    expectNoEvents()

                }

            }
        }
    }

    @Test
    fun register() {
        testCoroutineRule.runTest {
            `when`(remoteSource.registerUser(createRegisterPayload())).thenReturn(
                createRegisterResponseSuccess()
            )
            launchTest {
                val result = systemUnderTest.register(createRegisterForm())
                verify(remoteSource).registerUser(createRegisterPayload())

                result.test(timeout = Duration.ZERO) {
                    val data = expectMostRecentItem()
                    assertTrue(data.isRight())

                    val success = data.getOrElse { null }
                    assertNotNull(success)

                    expectNoEvents()

                }

            }
        }
    }

    @Test
    fun logout() {
        testCoroutineRule.runTest {
            `when`(localSource.removeLoginData()).thenReturn(
                createLogoutResponse()
            )
            launchTest {
                val result = systemUnderTest.logout()
                verify(localSource).removeLoginData()

                result.test(timeout = Duration.ZERO) {
                    val data = expectMostRecentItem()
                    assertTrue(data.isRight())

                    val success = data.getOrElse { null }
                    assertNotNull(success)

                    expectNoEvents()

                }

            }
        }
    }

    @Test
    fun getLoggedInUser() {
        testCoroutineRule.runTest {
            `when`(localSource.getLoginData()).thenReturn(
                createLoggedInResponse()
            )
            launchTest {
                val result = systemUnderTest.getLoggedInUser()
                verify(localSource).getLoginData()

                result.test(timeout = Duration.ZERO) {
                    val data = expectMostRecentItem()
                    assertTrue(data.isRight())

                    val success = data.getOrElse { null }
                    assertNotNull(success)

                    assertEquals(success, createUser())
                    expectNoEvents()

                }

            }
        }
    }
}