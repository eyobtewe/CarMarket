package com.cars.cars_marketplace.domain.usecase

import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.repository.CarRepository
import javax.inject.Inject

class RefreshCarsUseCase @Inject constructor(private val repository: CarRepository) {
    suspend operator fun invoke(page: Int = 1, limit: Int = 50): Resource<Unit> =
        repository.refreshCars(page, limit)
}

