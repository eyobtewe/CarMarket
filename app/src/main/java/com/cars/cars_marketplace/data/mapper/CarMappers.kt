package com.cars.cars_marketplace.data.mapper

import com.cars.cars_marketplace.data.local.entity.CarEntity
import com.cars.cars_marketplace.data.remote.dto.CarDto
import com.cars.cars_marketplace.domain.model.Car

/**
 * Maps CarDto to domain Car model
 */
fun CarDto.toDomain(): Car = Car(
        id = id,
        make = make,
        model = model,
        year = year,
        price = price,
        bodyType = bodyType,
        mileage = mileage,
        color = color,
        description = description,
        images = images,
        transmission = transmission,
        fuelType = fuelType,
        features = features,
        location = location,
        status = status,
        userId = userId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

/**
 * Maps domain Car to CarEntity for local storage
 */
fun Car.toEntity(): CarEntity = CarEntity(
        id = id,
        make = make,
        model = model,
        year = year,
        price = price,
        bodyType = bodyType,
        mileage = mileage,
        color = color,
        description = description,
        imagesSerialized = images.joinToString("|")
    )

/**
 * Maps CarEntity to domain Car model
 */
fun CarEntity.toDomain(): Car = Car(
        id = id,
        make = make,
        model = model,
        year = year,
        price = price,
        bodyType = bodyType,
        mileage = mileage,
        color = color,
        description = description,
        images = if (imagesSerialized.isNotEmpty()) imagesSerialized.split("|") else emptyList(),
        transmission = null, // Not stored in entity
        fuelType = null, // Not stored in entity
        features = emptyList(), // Not stored in entity
        location = null, // Not stored in entity
        status = null, // Not stored in entity
        userId = null, // Not stored in entity
        createdAt = null, // Not stored in entity
        updatedAt = null // Not stored in entity
    )

/**
 * Maps list of CarDtos to domain Cars
 */
fun List<CarDto>.toDomainCars(): List<Car> = map { it.toDomain() }

/**
 * Maps list of CarEntities to domain Cars
 */
fun List<CarEntity>.toDomainCarsFromEntities(): List<Car> = map { it.toDomain() }

/**
 * Maps list of domain Cars to CarEntities
 */
fun List<Car>.toEntity(): List<CarEntity> = map { it.toEntity() }