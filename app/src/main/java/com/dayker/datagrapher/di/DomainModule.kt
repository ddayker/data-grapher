package com.dayker.datagrapher.di

import com.dayker.datagrapher.domain.repository.AuthRepository
import com.dayker.datagrapher.domain.repository.ImageRepository
import com.dayker.datagrapher.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideCreatePieChartUseCase(): CreatePieChartUseCase {
        return CreatePieChartUseCase()
    }

    @Provides
    fun provideSaveImageUseCase(imageRepository: ImageRepository): SaveImageUseCase {
        return SaveImageUseCase(imageRepository = imageRepository)
    }

    @Provides
    fun provideShareImageUseCase(imageRepository: ImageRepository): ShareImageUseCase {
        return ShareImageUseCase(imageRepository = imageRepository)
    }

    @Provides
    fun provideSignUpByEmailUseCase(authRepository: AuthRepository): SignUpByEmailUseCase {
        return SignUpByEmailUseCase(authRepository = authRepository)
    }

    @Provides
    fun provideSignInByEmailUseCase(authRepository: AuthRepository): SignInByEmailUseCase {
        return SignInByEmailUseCase(authRepository = authRepository)
    }

    @Provides
    fun provideResetPasswordUseCase(authRepository: AuthRepository): ResetPasswordUseCase {
        return ResetPasswordUseCase(authRepository = authRepository)
    }
}