package dev.adryanev.dicodingstory.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.adryanev.dicodingstory.core.utils.connectivity.ConnectivityUtils
import dev.adryanev.dicodingstory.core.utils.connectivity.ConnectivityUtilsImpl
import dev.adryanev.dicodingstory.core.utils.resource.ResourceProvider
import dev.adryanev.dicodingstory.core.utils.resource.ResourceProviderImpl
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
    
}