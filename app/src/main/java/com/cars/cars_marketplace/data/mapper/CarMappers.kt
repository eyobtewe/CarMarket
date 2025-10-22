package com.cars.cars_marketplace.data.mapper

import com.cars.cars_marketplace.data.local.entity.CarEntity
import com.cars.cars_marketplace.data.remote.dto.CarDto
import com.cars.cars_marketplace.domain.model.Car

private const val IMAGE_DELIMITER = "|"

fun CarDto.toEntity(): CarEntity = CarEntity(
    id = this.id,
    make = this.make,
    model = this.model,
    year = this.year,
    price = this.price,
    bodyType = this.bodyType,
    mileage = this.mileage,
    color = this.color,
    description = this.description,
    imagesSerialized = this.images.joinToString(IMAGE_DELIMITER)
)

fun CarEntity.toDomain(): Car = Car(
    id = this.id,
    make = this.make,
    model = this.model,
    year = this.year,
    price = this.price,
    bodyType = this.bodyType,
    mileage = this.mileage,
    color = this.color,
    description = this.description,
    images = if (this.imagesSerialized.isBlank()) emptyList() else this.imagesSerialized.split(IMAGE_DELIMITER),
    transmission = null, // Not stored in entity yet
    fuelType = null, // Not stored in entity yet
    features = emptyList(), // Not stored in entity yet
    location = null, // Not stored in entity yet
    status = null, // Not stored in entity yet
    userId = null, // Not stored in entity yet
    createdAt = null, // Not stored in entity yet
    updatedAt = null // Not stored in entity yet
)

fun Car.toEntity(): CarEntity = CarEntity(
    id = this.id,
    make = this.make,
    model = this.model,
    year = this.year,
    price = this.price,
    bodyType = this.bodyType,
    mileage = this.mileage,
    color = this.color,
    description = this.description,
    imagesSerialized = this.images.joinToString(IMAGE_DELIMITER)
)

// Direct mapping from DTO to Domain for search results
fun CarDto.toDomain(): Car = Car(
    id = this.id,
    make = this.make,
    model = this.model,
    year = this.year,
    price = this.price,
    bodyType = this.bodyType,
    mileage = this.mileage,
    color = this.color,
    description = this.description,
    images = this.images,
    transmission = this.transmission,
    fuelType = this.fuelType,
    features = this.features,
    location = this.location,
    status = this.status,
    userId = this.userId,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

