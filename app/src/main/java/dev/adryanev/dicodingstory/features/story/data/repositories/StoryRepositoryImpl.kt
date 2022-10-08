package dev.adryanev.dicodingstory.features.story.data.repositories

import arrow.core.Either
import dev.adryanev.dicodingstory.core.di.annotations.IoDispatcher
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.features.story.data.datasources.remote.StoryRemoteDataSource
import dev.adryanev.dicodingstory.features.story.data.models.CreateStoryPayload
import dev.adryanev.dicodingstory.features.story.data.models.toDomain
import dev.adryanev.dicodingstory.features.story.domain.entities.Story
import dev.adryanev.dicodingstory.features.story.domain.entities.StoryForm
import dev.adryanev.dicodingstory.features.story.domain.repositories.StoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(
    private val storyRemoteDataSource: StoryRemoteDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : StoryRepository {
    override suspend fun getLatestStory(): Flow<Either<Failure, List<Story>>> {
        return flow {
            val result = storyRemoteDataSource.getStories()
            val storyList = result.map { response ->
                response.listStory?.map {
                    it.toDomain()
                }!!
            }
            emit(storyList)
        }.flowOn(ioDispatcher)
    }

    override suspend fun addNewStory(storyForm: StoryForm): Flow<Either<Failure, Unit>> {
        return flow {
            val payload = CreateStoryPayload.fromDomain(storyForm)
            val photo = storyForm.photo

            val result = storyRemoteDataSource.addStory(payload, photo)

            emit(result.map { })
        }.flowOn(ioDispatcher)
    }

    override suspend fun addNewStoryAsGuest(storyForm: StoryForm): Flow<Either<Failure, Unit>> {
        return flow {
            val payload = CreateStoryPayload.fromDomain(storyForm)
            val photo = storyForm.photo

            val result = storyRemoteDataSource.addStoryAsGuest(payload, photo)

            emit(result.map { })
        }.flowOn(ioDispatcher)
    }

    override suspend fun getLatestStoryWithLocation(): Flow<Either<Failure, List<Story>>> =
        flow {
            val result = storyRemoteDataSource.getStoriesWithLocation()
            val storyList = result.map { response ->
                response.listStory?.map {
                    it.toDomain()
                }!!
            }
            emit(storyList)
        }.flowOn(ioDispatcher)

}