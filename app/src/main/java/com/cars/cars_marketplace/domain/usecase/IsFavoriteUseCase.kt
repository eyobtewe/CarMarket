package com.cars.cars_marketplace.domain.usecase

import kotlinx.coroutines.flow.Flow
import com.cars.cars_marketplace.domain.repository.FavoriteRepository
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository) {
    operator fun invoke(carId: String): Flow<Boolean> = repository.isFavorite(carId)
}

