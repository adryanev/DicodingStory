package dev.adryanev.dicodingstory.features.story.data.datasources.remote.services

import dev.adryanev.dicodingstory.features.story.data.models.CreateStoryPayload
import dev.adryanev.dicodingstory.features.story.data.models.CreateStoryResponse
import dev.adryanev.dicodingstory.features.story.data.models.StoryResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface AuthenticatedStoryService {

    @GET("stories")
    suspend fun getStories(): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @PartMap payload: CreateStoryPayload,
        @Part photo: MultipartBody.Part
    ): CreateStoryResponse
}