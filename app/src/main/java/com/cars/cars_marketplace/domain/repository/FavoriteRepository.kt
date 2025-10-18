package com.cars.cars_marketplace.domain.repository

import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorites(): Flow<List<Car>>
    suspend fun addFavorite(carId: String): Resource<Unit>
    suspend fun removeFavorite(carId: String): Resource<Unit>
    fun isFavorite(carId: String): Flow<Boolean>
}

