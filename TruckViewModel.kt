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

     var inRadiusCount: Int = 0
    private var periodicJob: Job? = null

    init {
        startPeriodicUpdate()
    }

    fun setAppInBackground(isInBackground: Boolean) {
        if (isInBackground) {
            inRadiusCount = _vehicles.value?.count { it.inRange } ?: 0
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
                val vehicleList = getAvailableTrucksUseCase()
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

    override fun onCleared() {
        super.onCleared()
        periodicJob?.cancel()
    }

    companion object {
        const val DELAY_TIME = 10000L // Refresh every 10 seconds
        const val DELAY_TIME_BACKGROUND = 60000L // 60 seconds
    }
}