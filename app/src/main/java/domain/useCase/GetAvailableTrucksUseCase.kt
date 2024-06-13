package domain.useCase

import domain.repository.TruckRepository

class GetAvailableTrucksUseCase(private val truckRepository: TruckRepository) {
    suspend operator fun invoke() =
        truckRepository.getAvailableTrucks()

}