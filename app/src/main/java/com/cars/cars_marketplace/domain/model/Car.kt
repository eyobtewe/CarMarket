package com.cars.cars_marketplace.domain.model

data class Car(
    val id: String,
    val make: String,
    val model: String,
    val year: Int,
    val price: Double,
    val bodyType: String? = null,
    val mileage: Int? = null,
    val color: String? = null,
    val description: String? = null,
    val images: List<String> = emptyList()
)

