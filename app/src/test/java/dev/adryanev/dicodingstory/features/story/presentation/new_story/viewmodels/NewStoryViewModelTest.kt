package dev.adryanev.dicodingstory.features.story.presentation.new_story.viewmodels

import arrow.core.getOrElse
import dev.adryanev.dicodingstory.core.*
import dev.adryanev.dicodingstory.core.domain.usecases.NoParams
import dev.adryanev.dicodingstory.core.presentation.viewmodels.BaseViewModelTest
import dev.adryanev.dicodingstory.core.utils.getOrAwaitValue
import dev.adryanev.dicodingstory.core.utils.mock
import dev.adryanev.dicodingstory.core.utils.resource.ResourceProvider
import dev.adryanev.dicodingstory.features.authentication.domain.usecases.GetLoggedInUser
import dev.adryanev.dicodingstory.features.story.domain.entities.StoryForm
import dev.adryanev.dicodingstory.features.story.domain.usecases.CreateNewStory
import dev.adryanev.dicodingstory.features.story.domain.usecases.CreateNewStoryParams
import dev.adryanev.dicodingstory.services.locations.domain.usecases.GetCurrentLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class NewStoryViewModelTest : BaseViewModelTest() {

    private lateinit var systemUnderTest: NewStoryViewModel
    private val getLoggedInUser = mock<GetLoggedInUser>()
    private val createNewStory = mock<CreateNewStory>()
    private val resourceProvider = mock<ResourceProvider>()
    private val getCurrentLocation = mock<GetCurrentLocation>()

    @Before
    fun setUp() {
        systemUnderTest =
            NewStoryViewModel(getLoggedInUser, createNewStory, resourceProvider, getCurrentLocation)
    }

    @Test
    fun `should return user when success`() {
        // get the private method through reflections
        val method = systemUnderTest.javaClass.getDeclaredMethod("getUser")
        method.isAccessible = true

        testCoroutineRule.runTest {
            Mockito.`when`(getLoggedInUser(NoParams)).thenReturn(createUserFound())
            launchTest {
                method.invoke(systemUnderTest)
                Mockito.verify(getLoggedInUser).invoke(NoParams)

                val state = systemUnderTest.state.getOrAwaitValue()
                val either = state.getLoggedInUser.getOrElse { null }
                Assert.assertNotNull(either)
                Assert.assertTrue(either!!.isRight())

                val user = either.getOrElse { null }
                Assert.assertNotNull(user)
                Assert.assertEquals(user, createUser())
            }
        }
    }

    @Test
    fun `should set loggedInUser state when called`() {
        testCoroutineRule.runTest {
            Mockito.`when`(getLoggedInUser(NoParams)).thenReturn(createUserFound())
            launchTest {
                Mockito.verify(getLoggedInUser).invoke(NoParams)

                val state = systemUnderTest.state.getOrAwaitValue()
                val either = state.getLoggedInUser.getOrElse { null }
                Assert.assertNotNull(either)
                Assert.assertTrue(either!!.isRight())

                val user = either.getOrElse { null }
                Assert.assertNotNull(user)
                Assert.assertEquals(user, createUser())

                systemUnderTest.setUser(user)

                val nextState = systemUnderTest.state.getOrAwaitValue()
                Assert.assertEquals(nextState.loggedInUser, user)

            }
        }
    }

    @Test
    fun `should set description state when called`() {
        testCoroutineRule.runTest {
            launchTest {
                val description = "Yo mamen"
                systemUnderTest.descriptionChanged(description)

                val state = systemUnderTest.state.getOrAwaitValue()
                Assert.assertEquals(state.description, description)
            }
        }
    }

    @Test
    fun `should set storyPicture state when called`() {
        testCoroutineRule.runTest {
            launchTest {
                val file = withContext(Dispatchers.IO) {
                    File.createTempFile("test", "test")
                }

                systemUnderTest.setStoryPicture(file)

                val state = systemUnderTest.state.getOrAwaitValue()
                Assert.assertEquals(state.storyPicture, file)
            }
        }
    }

    @Test
    fun `should return unit when story uploaded successfully`() {
        testCoroutineRule.runTest {
            Mockito.`when`(getLoggedInUser(NoParams)).thenReturn(createUserFound())
            Mockito.`when`(getCurrentLocation(NoParams)).thenReturn(createLocationSuccess())
            val file = createFile()
            val storyForm = StoryForm(description = "Yo mamen", createLocation(), file)
            Mockito.`when`(createNewStory(CreateNewStoryParams(storyForm))).thenReturn(
                createStorySuccess()
            )

            launchTest {
                Mockito.verify(getLoggedInUser).invoke(NoParams)

                val userState = systemUnderTest.state.getOrAwaitValue()
                val userEither = userState.getLoggedInUser.getOrElse { null }
                Assert.assertNotNull(userEither)
                Assert.assertTrue(userEither!!.isRight())

                val user = userEither.getOrElse { null }
                Assert.assertNotNull(user)
                Assert.assertEquals(user, createUser())

                systemUnderTest.setUser(user)

                val userNextState = systemUnderTest.state.getOrAwaitValue()
                Assert.assertEquals(userNextState.loggedInUser, user)

                systemUnderTest.getUserLocation()

                val locationState = systemUnderTest.state.getOrAwaitValue()
                val locationEither = locationState.getUserLocation.getOrElse { null }
                Assert.assertNotNull(locationEither)

                val location = locationEither?.getOrElse { null }
                Assert.assertNotNull(location)

                systemUnderTest.setUserLocation(location!!)
                val locationNewState = systemUnderTest.state.getOrAwaitValue()
                Assert.assertEquals(locationNewState.userLocation, location)



                systemUnderTest.setStoryPicture(file)

                val pictureState = systemUnderTest.state.getOrAwaitValue()
                Assert.assertEquals(pictureState.storyPicture, file)

                val description = "Yo mamen"
                systemUnderTest.descriptionChanged(description)

                val descriptionState = systemUnderTest.state.getOrAwaitValue()
                Assert.assertEquals(descriptionState.description, description)

                systemUnderTest.uploadStory()

                val uploadState = systemUnderTest.state.getOrAwaitValue()
                val uploadEither = uploadState.createNewStory.getOrElse { null }
                Assert.assertNotNull(uploadEither)
                Assert.assertTrue(uploadEither!!.isRight())
            }
        }
    }

    @Test
    fun `should reset state when called`() {
        testCoroutineRule.runTest {
            launchTest {
                systemUnderTest.reset()

                val state = systemUnderTest.state.getOrAwaitValue()
                Assert.assertEquals(state, NewStoryState.initial())
            }
        }
    }

    @Test
    fun `should fetch user location when called`() {
        testCoroutineRule.runTest {
            Mockito.`when`(getCurrentLocation(NoParams)).thenReturn(createLocationSuccess())
            launchTest {
                systemUnderTest.getUserLocation()

                val state = systemUnderTest.state.getOrAwaitValue()
                val either = state.getUserLocation.getOrElse { null }
                Assert.assertNotNull(either)

                val location = either?.getOrElse { null }
                Assert.assertNotNull(location)

            }
        }
    }

    @Test
    fun `should set userLocation state when called`() {
        testCoroutineRule.runTest {
            Mockito.`when`(getCurrentLocation(NoParams)).thenReturn(createLocationSuccess())
            launchTest {
                systemUnderTest.getUserLocation()

                val state = systemUnderTest.state.getOrAwaitValue()
                val either = state.getUserLocation.getOrElse { null }
                Assert.assertNotNull(either)

                val location = either?.getOrElse { null }
                Assert.assertNotNull(location)

                systemUnderTest.setUserLocation(location!!)
                val newState = systemUnderTest.state.getOrAwaitValue()
                Assert.assertEquals(newState.userLocation, location)
            }
        }
    }
}