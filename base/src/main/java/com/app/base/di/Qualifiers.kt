package com.app.base.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthorizedClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UnauthorizedClient
