package dev.adryanev.dicodingstory.core.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.adryanev.dicodingstory.core.di.annotations.AuthInterceptorOkHttp
import dev.adryanev.dicodingstory.core.di.annotations.NonAuthInterceptorOkHttp
import dev.adryanev.dicodingstory.core.di.annotations.PrivateRetrofit
import dev.adryanev.dicodingstory.core.di.annotations.PublicRetrofit
import dev.adryanev.dicodingstory.core.networks.interceptors.AuthInterceptor
import dev.adryanev.dicodingstory.core.networks.models.ErrorResponse
import dev.adryanev.dicodingstory.core.utils.NetworkConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    @NonAuthInterceptorOkHttp
    fun provideOkHttpWithoutAuth(): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor())
        .addNetworkInterceptor(StethoInterceptor())
        .build()

    @Singleton
    @Provides
    @AuthInterceptorOkHttp
    fun provideOkHttpWithAuth(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addNetworkInterceptor(HttpLoggingInterceptor())
            .addNetworkInterceptor(StethoInterceptor())
            .build()

    @Singleton
    @Provides
    @PublicRetrofit
    fun providePublicRetrofit(
        @NonAuthInterceptorOkHttp okHttp: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .client(okHttp)
            .baseUrl(NetworkConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Singleton
    @Provides
    @PrivateRetrofit
    fun providePrivateRetrofit(
        @AuthInterceptorOkHttp okHttp: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .client(okHttp)
            .baseUrl(NetworkConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Singleton
    @Provides
    fun provideErrorJsonAdapter(moshi: Moshi): JsonAdapter<ErrorResponse> =
        moshi.adapter(ErrorResponse::class.java)

}

