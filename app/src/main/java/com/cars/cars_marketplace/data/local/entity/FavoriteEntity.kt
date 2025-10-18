package com.cars.cars_marketplace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val carId: String,
    val addedAt: Long = System.currentTimeMillis()
)

