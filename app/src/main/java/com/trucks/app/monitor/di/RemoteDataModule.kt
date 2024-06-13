package com.trucks.app.monitor.di

import data.api.TrucksApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.TruckRemoteDataSource
import data.repository.dataSourceImpl.TruckRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataModule {
    @Provides
    fun provideTruckRemoteDataSource(trucksApi: TrucksApi): TruckRemoteDataSource =
        TruckRemoteDataSourceImpl(trucksApi)
}
