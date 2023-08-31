package com.dayker.datagrapher.di

import android.content.Context
import com.dayker.datagrapher.data.repository.AuthRepositoryImpl
import com.dayker.datagrapher.data.repository.GoogleAuthRepositoryImpl
import com.dayker.datagrapher.data.repository.ImageRepositoryImpl
import com.dayker.datagrapher.data.storage.AndroidResourceManager
import com.dayker.datagrapher.data.storage.ResourceManager
import com.dayker.datagrapher.domain.repository.AuthRepository
import com.dayker.datagrapher.domain.repository.GoogleAuthRepository
import com.dayker.datagrapher.domain.repository.ImageRepository
import com.google.android.gms.auth.api.identity.Identity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideResourceManager(@ApplicationContext context: Context): ResourceManager {
        return AndroidResourceManager(context = context)
    }

    @Provides
    @Singleton
    fun provideImageRepository(
        resourceManager: ResourceManager,
        @ApplicationContext context: Context
    ): ImageRepository {
        return ImageRepositoryImpl(
            resourceManager = resourceManager,
            resolver = context.contentResolver
        )
    }

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return AuthRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideGoogleAuthRepository(@ApplicationContext context: Context): GoogleAuthRepository {
        val signInClient = Identity.getSignInClient(context)
        return GoogleAuthRepositoryImpl(oneTapClient = signInClient)
    }
}