package com.cars.cars_marketplace.presentation.common.ui

sealed interface ChatUiState {
    object Idle : ChatUiState
    object Loading : ChatUiState
    data class Error(val message: String) : ChatUiState
}
