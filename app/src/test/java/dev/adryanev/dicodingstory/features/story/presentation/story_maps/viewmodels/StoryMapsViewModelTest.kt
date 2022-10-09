package dev.adryanev.dicodingstory.features.story.presentation.story_maps.viewmodels

import arrow.core.getOrElse
import dev.adryanev.dicodingstory.core.createStory
import dev.adryanev.dicodingstory.core.createStoryDataFailure
import dev.adryanev.dicodingstory.core.createStoryDataSuccess
import dev.adryanev.dicodingstory.core.domain.usecases.NoParams
import dev.adryanev.dicodingstory.core.presentation.viewmodels.BaseViewModelTest
import dev.adryanev.dicodingstory.core.utils.getOrAwaitValue
import dev.adryanev.dicodingstory.core.utils.mock
import dev.adryanev.dicodingstory.features.story.domain.entities.Story
import dev.adryanev.dicodingstory.features.story.domain.usecases.GetLatestStoryWithLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

@OptIn(ExperimentalCoroutinesApi::class)

class StoryMapsViewModelTest : BaseViewModelTest() {

    private val getLatestStoryWithLocation = mock<GetLatestStoryWithLocation>()
    private lateinit var systemUnderTest: StoryMapsViewModel
    private lateinit var stories: List<Story>

    @Before
    fun setUp() {
        stories = createStory()
    }

    @Test
    fun getGetLatestStoryWithLocationSuccess() {
        testCoroutineRule.runTest {
            `when`(getLatestStoryWithLocation(NoParams)).thenReturn(createStoryDataSuccess())


            launchTest {
                systemUnderTest = StoryMapsViewModel(getLatestStoryWithLocation)


                val state = systemUnderTest.state.getOrAwaitValue()
                verify(getLatestStoryWithLocation).invoke(NoParams)

                val either = state.storyList.getOrElse { null }
                val data = either?.getOrElse { null }
                Assert.assertFalse(state.isLoading)
                Assert.assertNotNull(data)

                if (data != null) {
                    val story = data.firstOrNull()
                    Assert.assertNotNull(story)
                    Assert.assertEquals(stories, story)
                }

            }
        }
    }

    @Test
    fun getGetLatestStoryWithLocationFailed() {
        testCoroutineRule.runTest {
            `when`(getLatestStoryWithLocation(NoParams)).thenReturn(createStoryDataFailure())


            launchTest {
                systemUnderTest = StoryMapsViewModel(getLatestStoryWithLocation)


                val state = systemUnderTest.state.getOrAwaitValue()
                verify(getLatestStoryWithLocation).invoke(NoParams)

                val either = state.storyList.getOrElse { null }
                val data = either?.getOrElse { null }
                Assert.assertFalse(state.isLoading)
                Assert.assertNull(data)

            }
        }
    }
}