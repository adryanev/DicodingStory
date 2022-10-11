package dev.adryanev.dicodingstory.features.story.data.datasources.mediator

import androidx.paging.*
import androidx.room.withTransaction
import arrow.core.getOrElse
import arrow.core.getOrHandle
import dev.adryanev.dicodingstory.core.databases.DicodingStoryDatabase
import dev.adryanev.dicodingstory.features.story.data.datasources.local.StoryLocalDataSource
import dev.adryanev.dicodingstory.features.story.data.datasources.remote.StoryRemoteDataSource
import dev.adryanev.dicodingstory.features.story.data.models.StoryListResponse
import dev.adryanev.dicodingstory.features.story.data.models.StoryLocalModel
import dev.adryanev.dicodingstory.features.story.data.models.StoryRemoteKey
import okio.IOException
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class StoryMediator @Inject constructor(
    private val remoteDataSource: StoryRemoteDataSource,
    private val localDataSource: StoryLocalDataSource,
    private val storyDatabase: DicodingStoryDatabase,
) : RemoteMediator<Int, StoryLocalModel>() {

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, StoryLocalModel>
    ): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: FIRST_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKey?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKey != null
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                val nextKey = remoteKey?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKey != null
                )
                Timber.d("last remoteKey ${remoteKey.id}")
                Timber.d("nextKey = ${remoteKey.nextKey}")
                nextKey
            }
        }
        try {

            val result = remoteDataSource.getStories(page = loadKey, state.config.pageSize)
            if (result.isLeft()) {
                val failure = result.getOrHandle { it }
                return MediatorResult.Error(IOException(failure.toString()))
            }

            val data =
                result.getOrElse { null } ?: return MediatorResult.Error(IOException("Not Found"))
            val stories = data.listStory
            val endOfPageReached = data.listStory?.isEmpty() ?: true

            storyDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localDataSource.clearAllTable()
                }
                insertNewPageData(stories, endOfPageReached, loadKey)

            }
            return MediatorResult.Success(endOfPaginationReached = endOfPageReached)


        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }


    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, StoryLocalModel>
    ): StoryRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            storyDatabase.storyRemoteKeyDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, StoryLocalModel>
    ): StoryRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            storyDatabase.storyRemoteKeyDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, StoryLocalModel>
    ): StoryRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            storyDatabase.storyRemoteKeyDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun insertNewPageData(
        data: List<StoryListResponse>?, endOfPageReached: Boolean, page: Int
    ) {

        val prevKey = if (page == 1) null else page - 1
        val nextKey = if (endOfPageReached) null else page + 1

        val key = data?.map {
            StoryRemoteKey(
                id = it.id!!, nextKey = nextKey, prevKey = prevKey
            )
        }
        if (key != null) {
            localDataSource.insertAllRemoteKeys(key)
        }
        if (data != null) {
            localDataSource.insertAllStories(data.map { StoryLocalModel.fromResponse(it) })
        }
    }

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH


    private companion object {
        const val FIRST_PAGE = 1
    }
}