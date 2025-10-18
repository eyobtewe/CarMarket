package com.cars.cars_marketplace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cars.cars_marketplace.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE carId = :carId")
    suspend fun deleteFavorite(carId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE carId = :carId)")
    fun isFavorite(carId: String): Flow<Boolean>
}

