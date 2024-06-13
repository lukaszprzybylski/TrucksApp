package domain.repository

import data.model.TruckResponse
import domain.util.Result

interface TruckRepository {
    suspend fun getAvailableTrucks(): Result<TruckResponse>
}