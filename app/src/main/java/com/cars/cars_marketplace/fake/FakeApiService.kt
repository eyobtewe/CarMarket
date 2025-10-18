package com.cars.cars_marketplace.fake

import com.cars.cars_marketplace.data.remote.api.ApiService
import com.cars.cars_marketplace.data.remote.dto.*
import kotlinx.coroutines.delay
import java.util.*

class FakeApiService : ApiService {

    private val sampleCars = List(8) { idx ->
        CarDto(
            id = UUID.randomUUID().toString(),
            make = listOf("Toyota", "Honda", "Ford", "BMW")[idx % 4],
            model = "Model ${idx + 1}",
            year = 2015 + idx,
            price = 15000.0 + idx * 2000,
            images = listOf("https://placehold.co/600x400?text=Car+${idx+1}")
        )
    }

    override suspend fun register(request: RegisterRequest): AuthResponse {
        delay(200)
        val user = UserDto(UUID.randomUUID().toString(), request.firstName, request.lastName, request.email)
        return AuthResponse(token = "fake_token_${user.id}", user = user)
    }

    override suspend fun login(request: LoginRequest): AuthResponse {
        delay(200)
        val user = UserDto(UUID.randomUUID().toString(), "Demo", "User", request.email)
        return AuthResponse(token = "fake_token_${user.id}", user = user)
    }

    override suspend fun getCurrentUser(): UserDto {
        delay(100)
        return UserDto("user-id", "Demo", "User", "demo@example.com")
    }

    override suspend fun getCars(page: Int, limit: Int, make: String?, bodyType: String?, minPrice: Int?, maxPrice: Int?): CarsResponse {
        delay(250)
        return CarsResponse(items = sampleCars, page = page, total = sampleCars.size)
    }

    override suspend fun getCarById(id: String): CarDto {
        delay(150)
        return sampleCars.firstOrNull { it.id == id } ?: sampleCars.first()
    }

    override suspend fun searchCars(query: String): CarsResponse {
        delay(150)
        val filtered = sampleCars.filter { it.make.contains(query, ignoreCase = true) || it.model.contains(query, ignoreCase = true) }
        return CarsResponse(items = filtered, page = 1, total = filtered.size)
    }

    override suspend fun getFavorites(): List<CarDto> {
        delay(100)
        return sampleCars.take(2)
    }

    override suspend fun addFavorite(request: FavoriteRequest): ApiResponse {
        delay(100)
        return ApiResponse(success = true, message = "Added")
    }

    override suspend fun removeFavorite(carId: String): ApiResponse {
        delay(100)
        return ApiResponse(success = true, message = "Removed")
    }

    override suspend fun chat(request: AiChatRequest): AiChatResponse {
        delay(300)
        return AiChatResponse(
            message = "Fake AI reply for: ${request.message}",
            recommendedCars = sampleCars.take(2).map { SimpleCar(it.id, it.make, it.model) }
        )
    }
}

