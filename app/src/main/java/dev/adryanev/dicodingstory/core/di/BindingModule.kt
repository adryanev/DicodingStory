package dev.adryanev.dicodingstory.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.adryanev.dicodingstory.core.utils.connectivity.ConnectivityUtils
import dev.adryanev.dicodingstory.core.utils.connectivity.ConnectivityUtilsImpl
import dev.adryanev.dicodingstory.core.utils.resource.ResourceProvider
import dev.adryanev.dicodingstory.core.utils.resource.ResourceProviderImpl
import dev.adryanev.dicodingstory.features.authentication.data.datasources.local.AuthenticationLocalDataSource
import dev.adryanev.dicodingstory.features.authentication.data.datasources.local.AuthenticationLocalDataSourceImpl
import dev.adryanev.dicodingstory.features.authentication.data.datasources.remote.AuthenticationRemoteDataSource
import dev.adryanev.dicodingstory.features.authentication.data.datasources.remote.AuthenticationRemoteDataSourceImpl
import dev.adryanev.dicodingstory.features.authentication.data.repositories.AuthenticationRepositoryImpl
import dev.adryanev.dicodingstory.features.authentication.domain.repositories.AuthenticationRepository
import dev.adryanev.dicodingstory.features.story.data.datasources.local.StoryLocalDataSource
import dev.adryanev.dicodingstory.features.story.data.datasources.local.StoryLocalDataSourceImpl
import dev.adryanev.dicodingstory.features.story.data.datasources.remote.StoryRemoteDataSource
import dev.adryanev.dicodingstory.features.story.data.datasources.remote.StoryRemoteDataSourceImpl
import dev.adryanev.dicodingstory.features.story.data.repositories.StoryRepositoryImpl
import dev.adryanev.dicodingstory.features.story.domain.repositories.StoryRepository
import dev.adryanev.dicodingstory.services.locations.data.repositories.LocationRepositoryImpl
import dev.adryanev.dicodingstory.services.locations.domain.repositories.LocationRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindingModule {

    @Singleton
    @Binds
    abstract fun bindConnectivity(connectivityUtilsImpl: ConnectivityUtilsImpl): ConnectivityUtils

    @Singleton
    @Binds
    abstract fun bindResourceProvider(resourceProviderImpl: ResourceProviderImpl): ResourceProvider

    // =====================LOCATION BINDINGS==============================

    @Singleton
    @Binds
    abstract fun bindLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository


    // =====================AUTHENTICATION BINDINGS========================
    @Singleton
    @Binds
    abstract fun bindAuthenticationRemoteSource(
        authenticationRemoteDataSourceImpl: AuthenticationRemoteDataSourceImpl
    ): AuthenticationRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindAuthenticationLocalSource(
        authenticationLocalDataSourceImpl: AuthenticationLocalDataSourceImpl
    ): AuthenticationLocalDataSource

    @Singleton
    @Binds
    abstract fun bindAuthenticationRepository(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    // =====================STORY BINDINGS========================
    @Singleton
    @Binds
    abstract fun bindStoryRemoteSource(
        storyRemoteDataSource: StoryRemoteDataSourceImpl
    ): StoryRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindStoryLocalSource(
        storyLocalDataSourceImpl: StoryLocalDataSourceImpl
    ): StoryLocalDataSource

    @Singleton
    @Binds
    abstract fun bindStoryRepository(storyRepositoryImpl: StoryRepositoryImpl): StoryRepository
}