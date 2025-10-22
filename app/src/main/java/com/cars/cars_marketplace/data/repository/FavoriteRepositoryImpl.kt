package com.cars.cars_marketplace.data.repository

import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.repository.FavoriteRepository
import com.cars.cars_marketplace.util.DataStoreManager
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl @Inject constructor(private val dataStoreManager: DataStoreManager) :
        FavoriteRepository {

    override fun getFavoriteIds(): Flow<Set<String>> {
        return dataStoreManager.getFavorites()
    }

    override suspend fun addFavorite(carId: String): Resource<Unit> {
        return try {
            dataStoreManager.addFavorite(carId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override suspend fun removeFavorite(carId: String): Resource<Unit> {
        return try {
            dataStoreManager.removeFavorite(carId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override fun isFavorite(carId: String): Flow<Boolean> {
        return dataStoreManager.getFavorites().map { favorites -> favorites.contains(carId) }
    }
}
