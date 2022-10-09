package dev.adryanev.dicodingstory.features.story.domain.usecases

import androidx.paging.PagingData
import dev.adryanev.dicodingstory.features.story.domain.entities.Story
import dev.adryanev.dicodingstory.features.story.domain.repositories.StoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLatestStory @Inject constructor(
    private val storyRepository: StoryRepository
) {
    suspend operator fun invoke(): Flow<PagingData<Story>> {
        return storyRepository.getLatestStory()
    }
}