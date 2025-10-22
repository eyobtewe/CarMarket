package com.cars.cars_marketplace.data.repository

import com.cars.cars_marketplace.data.local.dao.CarDao
import com.cars.cars_marketplace.data.mapper.toDomain
import com.cars.cars_marketplace.data.mapper.toEntity
import com.cars.cars_marketplace.data.remote.api.ApiService
import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val carDao: CarDao
) : CarRepository {

    override fun getCars(
        page: Int,
        limit: Int,
        make: String?,
        model: String?,
        minPrice: Int?,
        maxPrice: Int?,
        year: Int?,
        bodyType: String?
    ): Flow<Resource<List<Car>>> = flow {
        emit(Resource.Loading())

        // Emit cached snapshot if available
        try {
            val cached = carDao.getAllCars().first()
            if (cached.isNotEmpty()) {
                emit(Resource.Success(cached.map { it.toDomain() }))
            }
        } catch (_: Exception) {
            // ignore cache read errors
        }

        // Try to fetch remote and update DB
        try {
            val response = api.getCars(
                page = page,
                limit = limit,
                make = make,
                bodyType = bodyType,
                minPrice = minPrice,
                maxPrice = maxPrice
            )
            val entities = response.data.rows.map { it.toEntity() }
            carDao.insertCars(entities)

            val latest = carDao.getAllCars().first()
            emit(Resource.Success(latest.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Resource.Error<List<Car>>(e.message ?: "Unknown error", e))
        }
    }.catch { e -> emit(Resource.Error<List<Car>>(e.message, e)) }
    
    override fun searchCars(query: String): Flow<Resource<List<Car>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.searchCars(query)
            val cars = response.data.map { it.toDomain() } // Use direct mapping to preserve all fields
            // Update cache with search results (convert to entities for storage)
            val entities = response.data.map { it.toEntity() }
            carDao.insertCars(entities)
            emit(Resource.Success(cars))
        } catch (e: Exception) {
            emit(Resource.Error<List<Car>>(e.message ?: "Unknown error", e))
        }
    }

    override fun getCarById(id: String): Flow<Resource<Car>> = flow {
        // Try DB first
        val cached = try { carDao.getCarById(id).first() } catch (_: Exception) { null }
        if (cached != null) {
            emit(Resource.Success(cached.toDomain()))
            return@flow
        }

        // Not in DB — fetch remote
        try {
            val dto = api.getCarById(id)
            val entity = dto.toEntity()
            carDao.insertCars(listOf(entity))
            emit(Resource.Success(entity.toDomain()))
        } catch (e: Exception) {
            emit(Resource.Error<Car>(e.message ?: "Unknown error", e))
        }
    }

    override suspend fun refreshCars(page: Int, limit: Int): Resource<Unit> {
        return try {
            val response = api.getCars(page = page, limit = limit)
            val entities = response.data.rows.map { it.toEntity() }
            carDao.insertCars(entities)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message, e)
        }
    }
}
