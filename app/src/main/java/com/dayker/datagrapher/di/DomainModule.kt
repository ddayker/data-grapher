package com.dayker.datagrapher.di

import android.content.Context
import com.dayker.datagrapher.domain.usecase.CreatePieChartUseCase
import com.dayker.datagrapher.domain.usecase.SaveChartAsImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideCreatePieChartUseCase(): CreatePieChartUseCase {
        return CreatePieChartUseCase()
    }

    @Provides
    fun provideSaveChartAsImageUseCase(@ApplicationContext context: Context): SaveChartAsImageUseCase {
        val resolver = context.contentResolver
        return SaveChartAsImageUseCase(resolver)
    }
}