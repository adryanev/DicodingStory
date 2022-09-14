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
import dev.adryanev.dicodingstory.features.authentication.data.datasources.networks.AuthenticationRemoteDataSource
import dev.adryanev.dicodingstory.features.authentication.data.datasources.networks.AuthenticationRemoteDataSourceImpl
import dev.adryanev.dicodingstory.features.authentication.data.repositories.AuthenticationRepositoryImpl
import dev.adryanev.dicodingstory.features.authentication.domain.repositories.AuthenticationRepository
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


    // AUTHENTICATION BINDINGS

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
}