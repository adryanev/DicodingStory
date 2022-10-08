package dev.adryanev.dicodingstory.features.story.data.datasources.local

import androidx.paging.PagingSource
import dev.adryanev.dicodingstory.features.story.data.models.StoryLocalModel
import dev.adryanev.dicodingstory.features.story.data.models.StoryRemoteKey

interface StoryLocalDataSource {
    suspend fun findMovieRemoteKeyById(id: String): StoryRemoteKey?
    fun getAllStories(): PagingSource<Int, StoryLocalModel>
    suspend fun insertAllRemoteKeys(list: List<StoryRemoteKey>)
    suspend fun insertAllStories(list: List<StoryLocalModel>)
    suspend fun clearAllTable()

}