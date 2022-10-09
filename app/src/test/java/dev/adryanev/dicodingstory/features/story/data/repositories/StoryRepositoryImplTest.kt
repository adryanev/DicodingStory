package dev.adryanev.dicodingstory.features.story.data.repositories

import androidx.paging.PagingConfig
import app.cash.turbine.test
import arrow.core.getOrElse
import arrow.core.right
import dev.adryanev.dicodingstory.core.*
import dev.adryanev.dicodingstory.core.data.repositories.BaseRepositoryTest
import dev.adryanev.dicodingstory.core.utils.collectData
import dev.adryanev.dicodingstory.core.utils.mock
import dev.adryanev.dicodingstory.features.story.data.datasources.local.StoryLocalDataSource
import dev.adryanev.dicodingstory.features.story.data.datasources.mediator.StoryMediator
import dev.adryanev.dicodingstory.features.story.data.datasources.remote.StoryRemoteDataSource
import dev.adryanev.dicodingstory.features.story.domain.entities.StoryForm
import dev.adryanev.dicodingstory.features.story.domain.repositories.StoryRepository
import dev.adryanev.dicodingstory.services.locations.domain.entities.Location
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import kotlin.time.Duration

@ExperimentalCoroutinesApi
class StoryRepositoryImplTest : BaseRepositoryTest() {

    private lateinit var systemUnderTest: StoryRepository
    private val storyRemoteSource = mock<StoryRemoteDataSource>()
    private val localDataSource = mock<StoryLocalDataSource>()
    private val remoteMediator = mock<StoryMediator>()

    @Before
    fun setUp() {
        systemUnderTest = StoryRepositoryImpl(
            storyRemoteDataSource = storyRemoteSource,
            localDataSource = localDataSource,
            remoteMediator = remoteMediator,
            pageConfig = PagingConfig(pageSize = 10),
            ioDispatcher = testCoroutineRule.testCoroutineDispatcher
        )
    }

    @Test
    fun `should return story paging data when success`() {
        testCoroutineRule.runTest {
            launchTest {
                val result = systemUnderTest.getLatestStory()

                result.test(Duration.ZERO) {
                    val data = expectMostRecentItem().collectData()
                    assertNotNull(data)
                    assertEquals(data, createStory())

                    expectNoEvents()
                }
            }
        }
    }

    @Test
    fun `should return unit when success adding story`() {
        testCoroutineRule.runTest {
            val file = createFile()
            val payload = createAddStoryPayload()
            `when`(storyRemoteSource.addStory(payload, file)).thenReturn(
                createAddStoryResponse().right()
            )
            launchTest {
                val result = systemUnderTest.addNewStory(
                    StoryForm(
                        "Description", Location(0.10301, 131.241), file
                    )
                )

                verify(storyRemoteSource).addStory(payload, file)

                result.test(Duration.ZERO) {

                    val data = expectMostRecentItem()
                    assertTrue(data.isRight())
                    assertNotNull(data.getOrElse { null })
                }
            }
        }
    }

    @Test
    fun `should return story with location when success`() {
        testCoroutineRule.runTest {
            `when`(storyRemoteSource.getStoriesWithLocation())
                .thenReturn(createStoryDataResponse().right())
            launchTest {
                val result = systemUnderTest.getLatestStoryWithLocation()
                verify(storyRemoteSource).getStoriesWithLocation()

                result.test(Duration.ZERO) {
                    val data = expectMostRecentItem()
                    assertTrue(data.isRight())

                    val story = data.getOrElse { null }
                    assertNotNull(story)
                    assertEquals(story, createStory())

                    story?.forEach {
                        assertNotNull(it.location)
                    }

                    expectNoEvents()
                }
            }
        }
    }
}