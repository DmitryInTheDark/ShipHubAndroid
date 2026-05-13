package com.app.shiphub.di

import android.content.Context
import com.app.data.UserRepository
import com.app.data.storage.preferences.LegalInfoPreferences
import com.app.data.storage.preferences.PhysicalInfoPreferences
import com.app.data.storage.preferences.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLegalInfoPreferences(@ApplicationContext context: Context): LegalInfoPreferences {
        return LegalInfoPreferences(context)
    }

    @Provides
    @Singleton
    fun providePhysicalInfoStorage(@ApplicationContext context: Context): PhysicalInfoPreferences {
        return PhysicalInfoPreferences(context)
    }

    @Provides
    @Singleton
    fun provideUserStorage(
        @ApplicationContext context: Context,
        legalInfoPreferences: LegalInfoPreferences,
        physicalInfoPreferences: PhysicalInfoPreferences
    ): UserPreferences {
        return UserPreferences(
            context,
            legalInfoPreferences,
            physicalInfoPreferences
        )
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userPreferences: UserPreferences
    ): UserRepository = UserRepository(userPreferences)

}