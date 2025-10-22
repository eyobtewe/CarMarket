package com.cars.cars_marketplace.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.usecase.GetCarByIdUseCase
import com.cars.cars_marketplace.domain.usecase.IsFavoriteUseCase
import com.cars.cars_marketplace.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CarDetailUiState {
    object Loading : CarDetailUiState
    data class Success(val car: Car) : CarDetailUiState
    data class Error(val message: String) : CarDetailUiState
}

@HiltViewModel
class CarDetailViewModel @Inject constructor(
    private val getCarByIdUseCase: GetCarByIdUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CarDetailUiState>(CarDetailUiState.Loading)
    val uiState: StateFlow<CarDetailUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun load(carId: String) {
        viewModelScope.launch {
            _uiState.value = CarDetailUiState.Loading
            getCarByIdUseCase(carId).collect { res ->
                when (res) {
                    is Resource.Loading -> _uiState.value = CarDetailUiState.Loading
                    is Resource.Success -> {
                        _uiState.value = CarDetailUiState.Success(res.data)
                    }
                    is Resource.Error -> _uiState.value = CarDetailUiState.Error(res.message ?: "Unknown error")
                }
            }
        }

        viewModelScope.launch {
            isFavoriteUseCase(carId).collect { fav ->
                _isFavorite.value = fav
            }
        }
    }

    fun toggleFavorite(carId: String) {
        viewModelScope.launch {
            toggleFavoriteUseCase(carId)
        }
    }
}

