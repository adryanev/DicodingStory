package dev.adryanev.dicodingstory.features.story.data.datasources.remote.services

import dev.adryanev.dicodingstory.features.story.data.models.CreateStoryPayload
import dev.adryanev.dicodingstory.features.story.data.models.CreateStoryResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface GuestStoryService {

    @Multipart
    @POST("stories/guest")
    suspend fun addNewStoryAsGuest(
        @PartMap payload: CreateStoryPayload,
        @Part photo: MultipartBody.Part
    ): CreateStoryResponse
}