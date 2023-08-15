package com.dayker.datagrapher.di

import com.dayker.datagrapher.domain.usecase.CreatePieChartUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideCreatePieChartUseCase() : CreatePieChartUseCase{
        return CreatePieChartUseCase()
    }
}