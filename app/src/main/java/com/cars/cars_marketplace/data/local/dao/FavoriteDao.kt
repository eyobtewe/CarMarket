package com.cars.cars_marketplace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cars.cars_marketplace.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT carId FROM favorites")
    fun getFavoriteIds(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE carId = :carId")
    suspend fun removeFavorite(carId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE carId = :carId LIMIT 1)")
    fun isFavorite(carId: String): Flow<Boolean>
}
