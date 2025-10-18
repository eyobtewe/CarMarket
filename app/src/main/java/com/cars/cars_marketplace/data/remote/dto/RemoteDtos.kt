package com.cars.cars_marketplace.data.remote.dto

// Auth
data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val firstName: String, val lastName: String, val email: String, val password: String)

data class AuthResponse(val token: String, val user: UserDto)

data class UserDto(val id: String, val firstName: String, val lastName: String, val email: String)

// Car DTOs
data class CarDto(
    val id: String,
    val make: String,
    val model: String,
    val year: Int,
    val price: Double,
    val bodyType: String? = null,
    val mileage: Int? = null,
    val color: String? = null,
    val description: String? = null,
    val images: List<String> = emptyList()
)

data class CarsResponse(
    val items: List<CarDto> = emptyList(),
    val page: Int = 1,
    val total: Int = 0
)

// Favorites and generic responses
data class FavoriteRequest(val carId: String)

data class ApiResponse(val success: Boolean, val message: String? = null)

// AI
data class AiChatRequest(val message: String)

data class AiChatResponse(val message: String, val recommendedCars: List<SimpleCar> = emptyList())

data class SimpleCar(val id: String, val make: String, val model: String)

