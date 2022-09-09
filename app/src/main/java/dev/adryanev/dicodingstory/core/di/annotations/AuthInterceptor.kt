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
annotation class AuthRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NonAuthRetrofit