package dev.adryanev.dicodingstory.core.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.adryanev.dicodingstory.core.databases.DicodingStoryDatabase
import dev.adryanev.dicodingstory.core.utils.DatabaseConstants
import dev.adryanev.dicodingstory.core.utils.PreferenceConstants
import dev.adryanev.dicodingstory.features.story.data.datasources.local.services.StoryLocalDao
import dev.adryanev.dicodingstory.features.story.data.datasources.local.services.StoryRemoteKeyDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            PreferenceConstants.PREFERENCE_NAME,
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): DicodingStoryDatabase =
        Room.databaseBuilder(
            context,
            DicodingStoryDatabase::class.java,
            DatabaseConstants.DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideStoryDao(database: DicodingStoryDatabase): StoryLocalDao = database.storyLocalDao()

    @Singleton
    @Provides
    fun provideStoryRemoteKeyDao(database: DicodingStoryDatabase): StoryRemoteKeyDao =
        database.storyRemoteKeyDao()
}