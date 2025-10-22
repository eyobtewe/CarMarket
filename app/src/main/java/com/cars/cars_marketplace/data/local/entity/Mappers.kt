package com.cars.cars_marketplace.data.local.entity

import com.cars.cars_marketplace.domain.model.Car

fun CarEntity.toCar(): Car {
    return Car(
        id = this.id,
        make = this.make,
        model = this.model,
        year = this.year,
        price = this.price,
        // Split the serialized string into a list, assuming a comma delimiter
        images = if (this.imagesSerialized.isNotBlank()) this.imagesSerialized.split(',') else emptyList(),
//        mileage = this.mileage?.toDouble() ?: 0.0,
        // Properties not in CarEntity are given default values
//        fuelType = "", // Not in CarEntity
//        transmission = "", // Not in CarEntity
//        engineSize = "", // Not in CarEntity
        description = this.description ?: "",
//        features = emptyList(), // Not in CarEntity
//        owner = "", // Not in CarEntity
//        contact = "", // Not in CarEntity
        bodyType = this.bodyType ?: "",
//        carLocation = "" // Not in CarEntity
    )
}
