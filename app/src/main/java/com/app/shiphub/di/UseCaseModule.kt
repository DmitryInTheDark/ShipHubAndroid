package com.app.shiphub.di

import com.app.data.UserRepository
import com.app.data.api.AuthApi
import com.app.data.use_cases.AuthUseCase
import com.app.data.use_cases.UserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideUserUseCase(
        userRepository: UserRepository
    ): UserUseCase{
        return UserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideAuthUseCase(
        authApi: AuthApi,
        userRepository: UserRepository
    ): AuthUseCase{
        return AuthUseCase(authApi, userRepository)
    }

}