package dev.adryanev.dicodingstory.features.story.data.datasources.local.services

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.adryanev.dicodingstory.core.utils.DatabaseConstants
import dev.adryanev.dicodingstory.features.story.data.models.StoryRemoteKey

@Dao
interface StoryRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<StoryRemoteKey>)

    @Query("SELECT * FROM ${DatabaseConstants.STORY_REMOTE_KEY} WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): StoryRemoteKey?

    @Query("DELETE FROM ${DatabaseConstants.STORY_REMOTE_KEY}")
    suspend fun deleteRemoteKeys()
}