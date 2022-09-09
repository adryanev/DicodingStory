package dev.adryanev.dicodingstory.core.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.adryanev.dicodingstory.core.di.annotations.AuthInterceptorOkHttp
import dev.adryanev.dicodingstory.core.di.annotations.NonAuthInterceptorOkHttp
import dev.adryanev.dicodingstory.core.di.annotations.NonAuthRetrofit
import dev.adryanev.dicodingstory.core.networks.interceptors.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {


    @Singleton
    @Provides
    @NonAuthInterceptorOkHttp
    fun provideOkHttpWithoutAuth(): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(StethoInterceptor())
        .build()

    @Singleton
    @Provides
    @AuthInterceptorOkHttp
    fun provideOkHttpWithAuth(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addNetworkInterceptor(StethoInterceptor())
        .build()

    @Singleton
    @Provides
    @NonAuthRetrofit
    fun provideRetrofitWithoutAuthInterceptor(
        @NonAuthInterceptorOkHttp okHttp: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .client(okHttp)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
}
