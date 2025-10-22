package com.cars.cars_marketplace.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.domain.repository.CarRepository
import com.cars.cars_marketplace.domain.usecase.GetFavoritesUseCase
import com.cars.cars_marketplace.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel
@Inject
constructor(
        private val getFavoritesUseCase: GetFavoritesUseCase,
        private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
        private val carRepository: CarRepository
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Car>>(emptyList())
    val favorites: StateFlow<List<Car>> = _favorites.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            combine(getFavoritesUseCase(), carRepository.getCars()) { favoriteIds, carsResource ->
                when (carsResource) {
                    is com.cars.cars_marketplace.domain.model.Resource.Success -> {
                        carsResource.data.filter { car -> favoriteIds.contains(car.id) }
                    }
                    else -> emptyList()
                }
            }.collect { favoriteCars ->
                _favorites.value = favoriteCars
            }
        }
    }

    fun toggleFavorite(carId: String) {
        viewModelScope.launch { toggleFavoriteUseCase(carId) }
    }
}
