package data.repository.dataSourceImpl

import data.TruckRemoteDataSource
import data.model.TruckResponse
import domain.repository.TruckRepository
import domain.util.Result
import retrofit2.Response

class TruckRepositoryImpl(private val truckRemoteDataSource: TruckRemoteDataSource) :
    TruckRepository {

    override suspend fun getAvailableTrucks() = responseToRequest(truckRemoteDataSource.getAvailableTrucks())

    private fun responseToRequest(response: Response<TruckResponse>): Result<TruckResponse> {
        if(response.isSuccessful){
            response.body()?.let {result->
                return Result.Success(result)
            }
        }
        return Result.Error(response.message())
    }
}