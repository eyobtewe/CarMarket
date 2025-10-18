package com.cars.cars_marketplace.domain.usecase

import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCarByIdUseCase @Inject constructor(private val repository: CarRepository) {
    operator fun invoke(id: String): Flow<Resource<Car>> = repository.getCarById(id)
}

