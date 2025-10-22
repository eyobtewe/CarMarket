package com.cars.cars_marketplace.domain.usecase

import com.cars.cars_marketplace.domain.repository.FavoriteRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase @Inject constructor(private val repository: FavoriteRepository) {
    operator fun invoke(): Flow<Set<String>> = repository.getFavoriteIds()
}
