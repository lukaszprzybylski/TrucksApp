package domain.useCase

import domain.repository.TruckRepository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.location.Location
import data.TruckItem
import data.model.TruckResponse
import domain.util.Result

class GetAvailableTrucksUseCase(private val truckRepository: TruckRepository) {
    suspend operator fun invoke(): List<TruckItem> {
        return withContext(Dispatchers.IO) {
            val truckResponse = truckRepository.getAvailableTrucks()
            getTruckByDistance(truckResponse)
        }
    }

    private fun getTruckByDistance(result: Result<TruckResponse>): List<TruckItem> {
        return result.data?.map {
            val vehicleLocation = Location("").apply {
                latitude = it.location.component2()
                longitude = it.location.component1()
            }
            val isInRadius = TARGET_LOCATION.distanceTo(vehicleLocation) <= DISTANCE
            TruckItem(it.vehicleId, isInRadius)
        } ?: emptyList()
    }

    companion object {
        const val DISTANCE = 1000 // 1km
        val TARGET_LOCATION = Location("").apply {
            latitude = 46.5223916
            longitude = 6.6314437
        }
    }
}