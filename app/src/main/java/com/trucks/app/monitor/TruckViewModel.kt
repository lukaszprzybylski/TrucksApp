package com.trucks.app.monitor

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import data.TruckItem
import data.model.TruckResponse
import domain.useCase.GetAvailableTrucksUseCase
import domain.util.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TruckViewModel @Inject constructor(
    private val getAvailableTrucksUseCase: GetAvailableTrucksUseCase
) : ViewModel() {

    private val _vehicles = MutableStateFlow<List<TruckItem>?>(emptyList())
    val vehicles: StateFlow<List<TruckItem>?> get() = _vehicles.asStateFlow()

    private val _hideNotification = MutableStateFlow(false)
    val hideNotification: StateFlow<Boolean> get() = _hideNotification.asStateFlow()

    private var truckResponse: Result<TruckResponse>? = null
    var inRadiusCount: Int = 0
    private var periodicJob: Job? = null

    init {
        startPeriodicUpdate()
    }

    fun setAppInBackground(isInBackground: Boolean) {
        if (isInBackground) {
            inRadiusCount = getTruckByDistance(truckResponse).count { it.inRange }
            viewModelScope.launch {
                delay(DELAY_TIME_BACKGROUND)
                periodicJob?.cancel()
                _hideNotification.value = true
            }
        } else {
            _hideNotification.value = false
            startPeriodicUpdate()
        }
    }

    private fun fetchTrucksData() {
        viewModelScope.launch {
            try {
                truckResponse = getAvailableTrucksUseCase()
                val vehicleList = getTruckByDistance(truckResponse)
                _vehicles.value = vehicleList
            } catch (e: Exception) {
                // Handle the error appropriately
            }
        }
    }

    private fun startPeriodicUpdate() {
        periodicJob?.cancel()
        periodicJob = viewModelScope.launch {
            while (isActive) {
                fetchTrucksData()
                delay(DELAY_TIME)
            }
        }
    }

    private fun getTruckByDistance(result: Result<TruckResponse>?): List<TruckItem> {
        val itemList = mutableListOf<TruckItem>()
        result?.data?.forEach {
            val vehicleLocation = Location("").apply {
                latitude = it.location.component2()
                longitude = it.location.component1()
            }
            val isInRadius = TARGET_LOCATION.distanceTo(vehicleLocation) <= DISTANCE
            itemList.add(TruckItem(it.vehicleId, isInRadius))
        }
        return itemList
    }

    override fun onCleared() {
        super.onCleared()
        periodicJob?.cancel()
    }

    companion object {
        const val DISTANCE = 1000 // 1km
        const val DELAY_TIME = 10000L // Refresh every 10 seconds
        const val DELAY_TIME_BACKGROUND = 60000L // 60 seconds
        val TARGET_LOCATION = Location("").apply {
            latitude = 46.5223916
            longitude = 6.6314437
        }
    }
}
