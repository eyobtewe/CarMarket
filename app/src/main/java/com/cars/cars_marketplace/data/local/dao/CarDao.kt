package com.cars.cars_marketplace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cars.cars_marketplace.data.local.entity.CarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {

    @Query("SELECT * FROM cars ORDER BY price DESC")
    fun getAllCars(): Flow<List<CarEntity>>

    @Query("SELECT * FROM cars WHERE id = :id LIMIT 1")
    fun getCarById(id: String): Flow<CarEntity?>

    @Query("SELECT * FROM cars WHERE id IN (:ids)")
    fun getCarsByIds(ids: List<String>): Flow<List<CarEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCars(cars: List<CarEntity>)

    @Query("DELETE FROM cars")
    suspend fun clearCars()
}
