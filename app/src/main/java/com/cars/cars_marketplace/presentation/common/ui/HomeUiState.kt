package com.cars.cars_marketplace.presentation.common.ui

import com.cars.cars_marketplace.domain.model.Car

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val cars: List<Car>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
