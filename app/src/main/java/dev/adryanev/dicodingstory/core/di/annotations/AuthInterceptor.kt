package dev.adryanev.dicodingstory.core.di.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NonAuthInterceptorOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PrivateRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PublicRetrofit