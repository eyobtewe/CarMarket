package com.cars.cars_marketplace.domain.usecase

import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.repository.FavoriteRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository) {
    suspend operator fun invoke(carId: String): Resource<Unit> = repository.addFavorite(carId)
}

