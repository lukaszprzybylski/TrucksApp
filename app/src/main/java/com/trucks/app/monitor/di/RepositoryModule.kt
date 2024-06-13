package com.trucks.app.monitor.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.TruckRemoteDataSource
import domain.repository.TruckRepository
import data.repository.dataSourceImpl.TruckRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideTrucksRepository(truckRemoteDataSource: TruckRemoteDataSource): TruckRepository =
        TruckRepositoryImpl(truckRemoteDataSource)
}
