package com.cars.cars_marketplace.data.remote.dto

// Car DTOs
data class CarDto(
    val id: String,
    val make: String,
    val model: String,
    val year: Int,
    val price: Double,
    val bodyType: String? = null,
    val mileage: Int? = null,
    val color: String? = null,
    val description: String? = null,
    val images: List<String> = emptyList(),
    val transmission: String? = null,
    val fuelType: String? = null,
    val features: List<String> = emptyList(),
    val location: String? = null,
    val status: String? = null,
    val userId: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class CarsResponse(val message: String, val data: CarsData, val success: Boolean)

data class CarsData(val count: Int, val rows: List<CarDto>)

// Search API returns data as direct array, not nested in rows
data class SearchResponse(val message: String, val data: List<CarDto>, val success: Boolean)

data class SimpleCar(val id: String, val make: String, val model: String)
