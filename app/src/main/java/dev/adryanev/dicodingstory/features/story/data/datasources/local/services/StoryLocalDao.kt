package dev.adryanev.dicodingstory.features.story.data.datasources.local.services

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.adryanev.dicodingstory.core.utils.DatabaseConstants
import dev.adryanev.dicodingstory.features.story.data.models.StoryLocalModel

@Dao
interface StoryLocalDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryLocalModel>)

    @Query("SELECT * FROM ${DatabaseConstants.STORY_TABLE}")
    fun getAllStories(): PagingSource<Int, StoryLocalModel>

    @Query("DELETE FROM ${DatabaseConstants.STORY_TABLE}")
    suspend fun deleteAll()
}