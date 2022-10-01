package dev.adryanev.dicodingstory.features.story.domain.usecases

import arrow.core.Either
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.domain.usecases.NoParams
import dev.adryanev.dicodingstory.core.domain.usecases.UseCase
import dev.adryanev.dicodingstory.features.story.domain.entities.Story
import dev.adryanev.dicodingstory.features.story.domain.repositories.StoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLatestStory @Inject constructor(
    private val storyRepository: StoryRepository
) : UseCase<List<Story>, NoParams>() {
    override suspend fun invoke(params: NoParams): Flow<Either<Failure, List<Story>>> {
        return storyRepository.getLatestStory()
    }
}