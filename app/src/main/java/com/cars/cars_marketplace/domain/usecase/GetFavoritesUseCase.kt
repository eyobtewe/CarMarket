package com.cars.cars_marketplace.domain.usecase

import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(private val repository: FavoriteRepository) {
    operator fun invoke(): Flow<List<Car>> = repository.getFavorites()
}

