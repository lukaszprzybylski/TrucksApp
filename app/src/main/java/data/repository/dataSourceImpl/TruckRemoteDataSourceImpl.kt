package data.repository.dataSourceImpl

import data.TruckRemoteDataSource
import data.api.TrucksApi

class TruckRemoteDataSourceImpl(private val trucksApi: TrucksApi) : TruckRemoteDataSource {
    override suspend fun getAvailableTrucks() =
        trucksApi.getAvailableTrucks()

}