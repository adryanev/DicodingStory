package dev.adryanev.dicodingstory.features.story.domain.usecases

import arrow.core.Either
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.domain.usecases.UseCase
import dev.adryanev.dicodingstory.features.story.domain.repositories.StoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateNewStory @Inject constructor(
    private val storyRepository: StoryRepository
) : UseCase<Unit, CreateNewStoryParams>() {
    override suspend fun invoke(params: CreateNewStoryParams): Flow<Either<Failure, Unit>> {
        return storyRepository.addNewStory(params.storyForm)
    }
}

