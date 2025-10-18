package com.cars.cars_marketplace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
    @PrimaryKey val id: String,
    val make: String,
    val model: String,
    val year: Int,
    val price: Double,
    val bodyType: String?,
    val mileage: Int?,
    val color: String?,
    val description: String?,
    // store images as a single delimiter-separated string
    val imagesSerialized: String = ""
)

