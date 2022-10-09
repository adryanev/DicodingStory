package dev.adryanev.dicodingstory.features.story.presentation.story_list.viewmodels

import arrow.core.getOrElse
import dev.adryanev.dicodingstory.core.createStory
import dev.adryanev.dicodingstory.core.createStoryPagingData
import dev.adryanev.dicodingstory.core.presentation.viewmodels.BaseViewModelTest
import dev.adryanev.dicodingstory.core.utils.collectData
import dev.adryanev.dicodingstory.core.utils.getOrAwaitValue
import dev.adryanev.dicodingstory.core.utils.mock
import dev.adryanev.dicodingstory.features.story.domain.usecases.GetLatestStory
import dev.adryanev.dicodingstory.features.story.domain.usecases.GetLatestStoryParams
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class StoryListViewModelTest : BaseViewModelTest() {

    private lateinit var systemUnderTest: StoryListViewModel
    private val getLatestStory = mock<GetLatestStory>()
    private val story = createStory()

    @Before
    fun setUp() {
    }

    @Test
    fun getStorySuccess() {
        testCoroutineRule.runTest {
            Mockito.`when`(getLatestStory.invoke(GetLatestStoryParams(1))).thenReturn(
                createStoryPagingData()
            )
            launchTest {
                systemUnderTest = StoryListViewModel(getLatestStory)
                Mockito.verify(getLatestStory).invoke(GetLatestStoryParams(1))
                val state = systemUnderTest.state.getOrAwaitValue()
                val story = state.storyList.getOrElse { null }
                Assert.assertNotNull(story)
                val data = story!!.collectData()

                Assert.assertEquals(data, story)


            }
        }
    }

    @Test
    fun refreshPage() {
        testCoroutineRule.runTest {
            Mockito.`when`(getLatestStory.invoke(GetLatestStoryParams(1))).thenReturn(
                createStoryPagingData()
            )
            launchTest {
                systemUnderTest = StoryListViewModel(getLatestStory)
                systemUnderTest.refreshPage()
                Mockito.verify(getLatestStory).invoke(GetLatestStoryParams(1))
                val state = systemUnderTest.state.getOrAwaitValue()
                val story = state.storyList.getOrElse { null }
                Assert.assertNotNull(story)
                val data = story!!.collectData()

                Assert.assertEquals(data, story)


            }
        }
    }


}