package com.cars.cars_marketplace.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.usecase.GetCarsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val cars: List<Car>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCarsUseCase: GetCarsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadCars()
    }

    fun loadCars(page: Int = 1, limit: Int = 50) {
        viewModelScope.launch {
            getCarsUseCase(page, limit).collect { res ->
                when (res) {
                    is Resource.Loading -> _uiState.value = HomeUiState.Loading
                    is Resource.Success -> _uiState.value = HomeUiState.Success(res.data)
                    is Resource.Error -> _uiState.value = HomeUiState.Error(res.message ?: "Unknown error")
                }
            }
        }
    }
}

