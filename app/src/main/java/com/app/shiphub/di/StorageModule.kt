package com.app.shiphub.di

import com.app.base.storage.TokenStorage
import com.app.base.storage.TokenStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {
    @Binds
    @Singleton
    abstract fun bindTokenStorage(tokenStorageImpl: TokenStorageImpl): TokenStorage
}
