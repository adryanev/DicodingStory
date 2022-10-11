package dev.adryanev.dicodingstory.core.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.adryanev.dicodingstory.features.story.data.datasources.local.services.StoryLocalDao
import dev.adryanev.dicodingstory.features.story.data.datasources.local.services.StoryRemoteKeyDao
import dev.adryanev.dicodingstory.features.story.data.models.StoryLocalModel
import dev.adryanev.dicodingstory.features.story.data.models.StoryRemoteKey

@Database(
    entities = [StoryLocalModel::class, StoryRemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class DicodingStoryDatabase : RoomDatabase() {
    abstract fun storyLocalDao(): StoryLocalDao
    abstract fun storyRemoteKeyDao(): StoryRemoteKeyDao
}