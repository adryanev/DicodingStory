package dev.adryanev.dicodingstory.features.story.data.datasources.remote.services

import dev.adryanev.dicodingstory.features.story.data.models.CreateStoryResponse
import dev.adryanev.dicodingstory.features.story.data.models.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface AuthenticatedStoryService {

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int?,
        @Query("size") size: Int? = 10
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?,
        @Part photo: MultipartBody.Part
    ): CreateStoryResponse

    @GET("stories?location=1")
    suspend fun getStoriesWithLocation(): StoryResponse
}