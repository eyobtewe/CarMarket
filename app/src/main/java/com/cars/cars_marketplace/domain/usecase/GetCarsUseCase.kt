package com.cars.cars_marketplace.domain.usecase

import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCarsUseCase @Inject constructor(private val repository: CarRepository) {
    operator fun invoke(page: Int = 1, limit: Int = 50): Flow<Resource<List<Car>>> =
        repository.getCars(page, limit)
}

