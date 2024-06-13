package com.trucks.app.monitor.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import domain.repository.TruckRepository
import domain.useCase.GetAvailableTrucksUseCase

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetAvailableTrucksUseCase(truckRepository: TruckRepository) =
        GetAvailableTrucksUseCase(truckRepository)
}