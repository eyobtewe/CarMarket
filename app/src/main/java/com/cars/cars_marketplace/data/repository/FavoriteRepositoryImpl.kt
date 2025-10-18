package com.cars.cars_marketplace.data.repository

import com.cars.cars_marketplace.data.local.dao.CarDao
import com.cars.cars_marketplace.data.local.dao.FavoriteDao
import com.cars.cars_marketplace.data.local.entity.FavoriteEntity
import com.cars.cars_marketplace.data.mapper.toDomain
import com.cars.cars_marketplace.data.remote.api.ApiService
import com.cars.cars_marketplace.data.remote.dto.FavoriteRequest
import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.repository.FavoriteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val favoriteDao: FavoriteDao,
    private val carDao: CarDao
) : FavoriteRepository {

    override fun getFavorites(): Flow<List<Car>> =
        favoriteDao.getAllFavorites()
            .map { favs -> favs.map { it.carId } }
            .flatMapLatest { ids ->
                if (ids.isEmpty()) flowOf(emptyList())
                else carDao.getCarsByIds(ids).map { entities -> entities.map { it.toDomain() } }
            }

    override suspend fun addFavorite(carId: String): Resource<Unit> {
        return try {
            val resp = api.addFavorite(FavoriteRequest(carId))
            if (!resp.success) return Resource.Error(resp.message ?: "Failed to add favorite")
            favoriteDao.insertFavorite(FavoriteEntity(carId))
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message, e)
        }
    }

    override suspend fun removeFavorite(carId: String): Resource<Unit> {
        return try {
            val resp = api.removeFavorite(carId)
            if (!resp.success) return Resource.Error(resp.message ?: "Failed to remove favorite")
            favoriteDao.deleteFavorite(carId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message, e)
        }
    }

    override fun isFavorite(carId: String) = favoriteDao.isFavorite(carId)
}
