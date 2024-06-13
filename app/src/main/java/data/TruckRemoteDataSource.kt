package data

import data.model.TruckResponse
import retrofit2.Response

interface TruckRemoteDataSource {
    suspend fun getAvailableTrucks(): Response<TruckResponse>
}