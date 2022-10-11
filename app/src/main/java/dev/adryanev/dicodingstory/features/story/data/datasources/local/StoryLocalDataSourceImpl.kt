package dev.adryanev.dicodingstory.features.story.data.datasources.local

import androidx.paging.PagingSource
import dev.adryanev.dicodingstory.features.story.data.datasources.local.services.StoryLocalDao
import dev.adryanev.dicodingstory.features.story.data.datasources.local.services.StoryRemoteKeyDao
import dev.adryanev.dicodingstory.features.story.data.models.StoryLocalModel
import dev.adryanev.dicodingstory.features.story.data.models.StoryRemoteKey
import javax.inject.Inject

class StoryLocalDataSourceImpl @Inject constructor(
    private val storyLocalDao: StoryLocalDao,
    private val storyRemoteKeyDao: StoryRemoteKeyDao
) : StoryLocalDataSource {
    override suspend fun findMovieRemoteKeyById(id: String): StoryRemoteKey? =
        storyRemoteKeyDao.getRemoteKeysId(id)

    override fun getAllStories(): PagingSource<Int, StoryLocalModel> =
        storyLocalDao.getAllStories()

    override suspend fun insertAllRemoteKeys(list: List<StoryRemoteKey>) =
        storyRemoteKeyDao.insertAll(list)

    override suspend fun insertAllStories(list: List<StoryLocalModel>) =
        storyLocalDao.insertStories(list)

    override suspend fun clearAllTable() {
        storyRemoteKeyDao.deleteRemoteKeys()
        storyLocalDao.deleteAll()
    }
}