package com.cars.cars_marketplace.presentation.common.ui

import com.cars.cars_marketplace.domain.model.Car

sealed interface CarDetailUiState {
    object Loading : CarDetailUiState
    data class Success(val car: Car) : CarDetailUiState
    data class Error(val message: String) : CarDetailUiState
}
