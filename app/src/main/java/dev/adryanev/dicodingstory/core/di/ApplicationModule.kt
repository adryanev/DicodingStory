package dev.adryanev.dicodingstory.core.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.adryanev.dicodingstory.core.di.annotations.PublicRetrofit
import dev.adryanev.dicodingstory.core.networks.middlewares.ConnectivityMiddleware
import dev.adryanev.dicodingstory.core.networks.middlewares.providers.MiddlewareProvider
import dev.adryanev.dicodingstory.core.networks.middlewares.providers.MiddlewareProviderImpl
import dev.adryanev.dicodingstory.core.utils.connectivity.ConnectivityUtils
import dev.adryanev.dicodingstory.core.utils.resource.ResourceProvider
import dev.adryanev.dicodingstory.features.authentication.data.datasources.networks.services.AuthenticationService
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .build()

    @Singleton
    @Provides
    fun provideAuthenticationService(@PublicRetrofit retrofit: Retrofit): AuthenticationService =
        retrofit.create()

    @Singleton
    @Provides
    fun provideMiddlewareProvider(
        resourceProvider: ResourceProvider,
        connectivityUtils: ConnectivityUtils
    ): MiddlewareProvider =
        MiddlewareProviderImpl.Builder().addMiddleware(
            networkMiddleware = ConnectivityMiddleware(
                resourceProvider = resourceProvider,
                connectivityUtils = connectivityUtils
            )
        ).build()
}