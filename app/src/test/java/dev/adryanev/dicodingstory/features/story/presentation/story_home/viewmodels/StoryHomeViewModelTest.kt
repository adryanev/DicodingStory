package dev.adryanev.dicodingstory.features.story.presentation.story_home.viewmodels

import arrow.core.getOrElse
import dev.adryanev.dicodingstory.core.createLogoutFailure
import dev.adryanev.dicodingstory.core.createLogoutSuccess
import dev.adryanev.dicodingstory.core.domain.usecases.NoParams
import dev.adryanev.dicodingstory.core.presentation.viewmodels.BaseViewModelTest
import dev.adryanev.dicodingstory.core.utils.getOrAwaitValue
import dev.adryanev.dicodingstory.core.utils.mock
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.LogoutUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class StoryHomeViewModelTest : BaseViewModelTest() {

    private lateinit var systemUnderTest: StoryHomeViewModel
    private val logoutUser = mock<LogoutUser>()

    @Before
    fun setUp() {
        systemUnderTest = StoryHomeViewModel(logoutUser)
    }

    @Test
    fun logoutSuccess() {
        testCoroutineRule.runTest {
            Mockito.`when`(logoutUser(NoParams)).thenReturn(createLogoutSuccess())
            launchTest {
                systemUnderTest.logout()

                Mockito.verify(logoutUser).invoke(NoParams)
                val state = systemUnderTest.state.getOrAwaitValue()

                val either = state.logout.getOrElse { null }
                Assert.assertNotNull(either)
                Assert.assertTrue(either!!.isRight())
            }
        }
    }

    @Test
    fun logoutFailed() {
        testCoroutineRule.runTest {
            Mockito.`when`(logoutUser(NoParams)).thenReturn(createLogoutFailure())
            launchTest {
                systemUnderTest.logout()
                val state = systemUnderTest.state.getOrAwaitValue()

                val either = state.logout.getOrElse { null }
                Assert.assertNotNull(either)
                Assert.assertTrue(either!!.isLeft())
            }
        }
    }
}