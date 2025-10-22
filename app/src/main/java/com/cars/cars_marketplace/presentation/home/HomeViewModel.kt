package com.cars.cars_marketplace.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.usecase.GetCarsUseCase
import com.cars.cars_marketplace.domain.usecase.GetFavoritesUseCase
import com.cars.cars_marketplace.domain.usecase.SearchCarsUseCase
import com.cars.cars_marketplace.domain.usecase.ToggleFavoriteUseCase
import com.cars.cars_marketplace.presentation.common.ui.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@HiltViewModel
class HomeViewModel
@Inject
constructor(
        private val getCarsUseCase: GetCarsUseCase,
        private val searchCarsUseCase: SearchCarsUseCase,
        private val getFavoritesUseCase: GetFavoritesUseCase,
        private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    // Search & filters
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _bodyType = MutableStateFlow<String?>(null)
    val bodyType: StateFlow<String?> = _bodyType.asStateFlow()

    private val _maxPrice = MutableStateFlow<Int?>(null)
    val maxPrice: StateFlow<Int?> = _maxPrice.asStateFlow()

    // track favorite IDs
    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    // track pending toggle operations to disable UI while in-flight
    private val _pendingFavorites = MutableStateFlow<Set<String>>(emptySet())
    val pendingFavorites: StateFlow<Set<String>> = _pendingFavorites.asStateFlow()

    init {
        loadCars()
        observeFavorites()
    }

    fun loadCars(page: Int = 1, limit: Int = 50) {
        viewModelScope.launch {
            getCarsUseCase(page, limit).collect { res ->
                when (res) {
                    is Resource.Loading -> _uiState.value = HomeUiState.Loading
                    is Resource.Success -> _uiState.value = HomeUiState.Success(res.data)
                    is Resource.Error ->
                            _uiState.value = HomeUiState.Error(res.message ?: "Unknown error")
                }
            }
        }
    }

    fun updateQuery(q: String) {
        _query.value = q
        // Twitter-like search: search as you type with debounce
        if (q.trim().isNotEmpty()) {
            viewModelScope.launch {
                delay(300) // 300ms debounce like Twitter
                if (_query.value == q) { // Only search if query hasn't changed
                    applyFilters()
                }
            }
        } else {
            // If query is empty, load all cars
            loadCars()
        }
    }

    fun setBodyType(type: String?) {
        _bodyType.value = type
        applyFilters()
    }

    fun setMaxPrice(price: Int?) {
        _maxPrice.value = price
        applyFilters()
    }

    private fun applyFilters() {
        val q = _query.value.trim()

        if (q.isNotEmpty()) {
            // Twitter-like search: use search API for queries
            viewModelScope.launch {
                _uiState.value = HomeUiState.Loading
                searchCarsUseCase(q).collect { res ->
                    when (res) {
                        is Resource.Loading -> _uiState.value = HomeUiState.Loading
                        is Resource.Success -> {
                            // Apply additional local filters
                            val type = _bodyType.value?.lowercase()
                            val max = _maxPrice.value
                            val filtered = res.data.filter { car ->
                                (type == null || (car.bodyType ?: "").lowercase() == type) &&
                                (max == null || car.price <= max)
                            }
                            _uiState.value = HomeUiState.Success(filtered)
                        }
                        is Resource.Error ->
                            _uiState.value = HomeUiState.Error(res.message ?: "Search failed")
                    }
                }
            }
        } else {
            // Load all cars with filters
            val type = _bodyType.value
            val max = _maxPrice.value
            viewModelScope.launch {
                getCarsUseCase(bodyType = type, maxPrice = max).collect { res ->
                    when (res) {
                        is Resource.Loading -> _uiState.value = HomeUiState.Loading
                        is Resource.Success -> _uiState.value = HomeUiState.Success(res.data)
                        is Resource.Error ->
                            _uiState.value = HomeUiState.Error(res.message ?: "Unknown error")
                    }
                }
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase().collect { favoriteIds -> _favoriteIds.value = favoriteIds }
        }
    }

    fun toggleFavorite(carId: String) {
        viewModelScope.launch {
            // prevent duplicate in-flight toggles
            if (_pendingFavorites.value.contains(carId)) return@launch

            // optimistic update
            val current = _favoriteIds.value
            val willBeFavorite = !current.contains(carId)
            _favoriteIds.value = if (willBeFavorite) current + carId else current - carId

            // mark pending
            _pendingFavorites.value = _pendingFavorites.value + carId

            val res = toggleFavoriteUseCase(carId)
            // remove pending flag
            _pendingFavorites.value = _pendingFavorites.value - carId

            if (res is Resource.Error) {
                // revert on error
                _favoriteIds.value = current
                // optionally log/show error
            }
            // On success we rely on observeFavorites() or the optimistic update
        }
    }
}
