package com.cars.cars_marketplace.domain.repository

import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    fun getCars(page: Int = 1, limit: Int = 50): Flow<Resource<List<Car>>>
    fun getCarById(id: String): Flow<Resource<Car>>
    suspend fun refreshCars(page: Int = 1, limit: Int = 50): Resource<Unit>
}

