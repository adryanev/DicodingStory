package dev.adryanev.dicodingstory.features.story.data.datasources.remote

import arrow.core.Either
import com.squareup.moshi.JsonAdapter
import dev.adryanev.dicodingstory.core.di.annotations.IoDispatcher
import dev.adryanev.dicodingstory.core.domain.failures.Failure
import dev.adryanev.dicodingstory.core.networks.middlewares.extensions.safeCall
import dev.adryanev.dicodingstory.core.networks.middlewares.providers.MiddlewareProvider
import dev.adryanev.dicodingstory.core.networks.models.ErrorResponse
import dev.adryanev.dicodingstory.features.story.data.datasources.remote.services.AuthenticatedStoryService
import dev.adryanev.dicodingstory.features.story.data.datasources.remote.services.GuestStoryService
import dev.adryanev.dicodingstory.features.story.data.models.CreateStoryPayload
import dev.adryanev.dicodingstory.features.story.data.models.CreateStoryResponse
import dev.adryanev.dicodingstory.features.story.data.models.StoryResponse
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class StoryRemoteDataSourceImpl @Inject constructor(
    private val authenticatedStoryService: AuthenticatedStoryService,
    private val guestStoryService: GuestStoryService,
    private val middlewareProvider: MiddlewareProvider,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val adapter: JsonAdapter<ErrorResponse>,
) : StoryRemoteDataSource {
    override suspend fun getStories(): Either<Failure, StoryResponse> {
        return safeCall(
            middlewares = middlewareProvider.getAll(),
            ioDispatcher = ioDispatcher,
            adapter = adapter,
            retrofitCall = {
                authenticatedStoryService.getStories()
            }
        )
    }

    override suspend fun addStory(
        payload: CreateStoryPayload,
        photo: File
    ): Either<Failure, CreateStoryResponse> {
        return safeCall(
            middlewares = middlewareProvider.getAll(),
            ioDispatcher = ioDispatcher,
            adapter = adapter,
            retrofitCall = {
                authenticatedStoryService.addNewStory(
                    description = payload.description?.toRequestBody("text/plain".toMediaType())
                        ?: "".toRequestBody(),

                    photo = MultipartBody.Part.createFormData(
                        "photo", photo.name,
                        photo.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    ),
                    lat = payload.latitude?.toString()?.toRequestBody("text/plain".toMediaType()),
                    lon = payload.longitude?.toString()?.toRequestBody("text/plain".toMediaType()),
                )
            }
        )
    }

    override suspend fun addStoryAsGuest(
        payload: CreateStoryPayload,
        photo: File
    ): Either<Failure, CreateStoryResponse> {
        return safeCall(
            middlewares = middlewareProvider.getAll(),
            ioDispatcher = ioDispatcher,
            adapter = adapter,
            retrofitCall = {
                guestStoryService.addNewStoryAsGuest(
                    payload = payload,
                    photo = MultipartBody.Part.createFormData(
                        "photo", photo.name,
                        photo.asRequestBody()
                    )
                )
            }
        )
    }
}