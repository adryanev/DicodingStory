package dev.adryanev.dicodingstory.features.splash.presentation.viewmodels

import arrow.core.getOrElse
import dev.adryanev.dicodingstory.core.createUser
import dev.adryanev.dicodingstory.core.createUserFound
import dev.adryanev.dicodingstory.core.createUserNotFound
import dev.adryanev.dicodingstory.core.domain.usecases.NoParams
import dev.adryanev.dicodingstory.core.presentation.viewmodels.BaseViewModelTest
import dev.adryanev.dicodingstory.core.utils.getOrAwaitValue
import dev.adryanev.dicodingstory.core.utils.mock
import dev.adryanev.dicodingstory.features.authentication.domain.entities.User
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.GetLoggedInUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest : BaseViewModelTest() {

    private lateinit var systemUnderTest: SplashViewModel
    private val getLoggedInUser = mock<GetLoggedInUser>()
    private lateinit var user: User

    @Before
    fun setUp() {
        systemUnderTest = SplashViewModel(getLoggedInUser)
        user = createUser()
    }


    @Test
    fun `should return user when user is logged in`() {
        testCoroutineRule.runTest {
            Mockito.`when`(getLoggedInUser(NoParams))
                .thenReturn(createUserFound())

            launchTest {
                systemUnderTest.checkIsLoggedIn()

                Mockito.verify(getLoggedInUser).invoke(NoParams)

                val state = systemUnderTest.state.getOrAwaitValue()

                val either = state.checkLoginOrFailure.getOrElse { null }
                val data = either?.getOrElse { null }
                assertFalse(state.isLoading)
                assertNotNull(data)
            }
        }
    }

    @Test
    fun `should return null when user is not logged in`() {
        testCoroutineRule.runTest {
            Mockito.`when`(getLoggedInUser(NoParams))
                .thenReturn(createUserNotFound())

            launchTest {
                systemUnderTest.checkIsLoggedIn()

                Mockito.verify(getLoggedInUser).invoke(NoParams)

                val state = systemUnderTest.state.getOrAwaitValue()

                val either = state.checkLoginOrFailure.getOrElse { null }
                val data = either?.getOrElse { null }
                assertFalse(state.isLoading)
                assertNull(data)
            }
        }
    }

}