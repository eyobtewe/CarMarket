package com.cars.cars_marketplace.data.mapper

import com.cars.cars_marketplace.data.remote.dto.CarDto
import com.cars.cars_marketplace.domain.model.Car

/**
 * Maps AI-related data between different representations
 */
object AiMappers {

    /**
     * Maps AI recommended cars from DTOs to domain models
     */
    fun List<CarDto>.toDomainCars(): List<Car> = map { carDto ->
        Car(
            id = carDto.id,
            make = carDto.make,
            model = carDto.model,
            year = carDto.year,
            price = carDto.price,
            bodyType = carDto.bodyType,
            mileage = carDto.mileage,
            color = carDto.color,
            description = carDto.description,
            images = carDto.images,
            transmission = carDto.transmission,
            fuelType = carDto.fuelType,
            features = carDto.features,
            location = carDto.location,
            status = carDto.status,
            userId = carDto.userId,
            createdAt = carDto.createdAt,
            updatedAt = carDto.updatedAt
        )
    }
}
