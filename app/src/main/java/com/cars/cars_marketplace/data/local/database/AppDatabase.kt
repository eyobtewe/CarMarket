package com.cars.cars_marketplace.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cars.cars_marketplace.data.local.dao.CarDao
import com.cars.cars_marketplace.data.local.dao.FavoriteDao
import com.cars.cars_marketplace.data.local.entity.CarEntity
import com.cars.cars_marketplace.data.local.entity.FavoriteEntity

@Database(entities = [CarEntity::class, FavoriteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun favoriteDao(): FavoriteDao
}

