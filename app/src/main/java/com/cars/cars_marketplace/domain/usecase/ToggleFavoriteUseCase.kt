package com.cars.cars_marketplace.domain.usecase

import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository) {
    suspend operator fun invoke(carId: String): Resource<Unit> {
        val currently = repository.isFavorite(carId).first()
        return if (currently) repository.removeFavorite(carId) else repository.addFavorite(carId)
    }
}

