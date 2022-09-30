package dev.adryanev.dicodingstory.features.story.data.datasources.remote

import arrow.core.Either
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.features.story.data.models.CreateStoryPayload
import dev.adryanev.dicodingstory.features.story.data.models.CreateStoryResponse
import dev.adryanev.dicodingstory.features.story.data.models.StoryResponse
import java.io.File

interface StoryRemoteDataSource {
    suspend fun getStories(): Either<Failure, StoryResponse>

    suspend fun addStory(payload: CreateStoryPayload, photo: File): Either<Failure, CreateStoryResponse>

    suspend fun addStoryAsGuest(payload: CreateStoryPayload, photo: File): Either<Failure, CreateStoryResponse>

}