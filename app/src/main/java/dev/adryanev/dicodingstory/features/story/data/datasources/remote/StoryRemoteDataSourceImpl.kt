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
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject
import java.io.File

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
                    payload = payload,
                    photo = MultipartBody.Part.createFormData(
                        "photo", photo.name,
                        photo.asRequestBody()
                    )
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