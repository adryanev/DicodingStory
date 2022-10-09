package dev.adryanev.dicodingstory.features.story.domain.repositories

import androidx.paging.PagingData
import arrow.core.Either
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.features.story.domain.entities.Story
import dev.adryanev.dicodingstory.features.story.domain.entities.StoryForm
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    suspend fun getLatestStory(): Flow<PagingData<Story>>
    suspend fun addNewStory(storyForm: StoryForm): Flow<Either<Failure, Unit>>
    suspend fun getLatestStoryWithLocation(): Flow<Either<Failure, List<Story>>>
}