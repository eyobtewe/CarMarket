package com.cars.cars_marketplace.data.remote.api

import com.cars.cars_marketplace.data.remote.dto.AuthResponse
import com.cars.cars_marketplace.data.remote.dto.AiChatRequest
import com.cars.cars_marketplace.data.remote.dto.AiChatResponse
import com.cars.cars_marketplace.data.remote.dto.ApiResponse
import com.cars.cars_marketplace.data.remote.dto.CarDto
import com.cars.cars_marketplace.data.remote.dto.CarsResponse
import com.cars.cars_marketplace.data.remote.dto.FavoriteRequest
import com.cars.cars_marketplace.data.remote.dto.LoginRequest
import com.cars.cars_marketplace.data.remote.dto.RegisterRequest
import com.cars.cars_marketplace.data.remote.dto.UserDto
import retrofit2.http.*

interface ApiService {
    // Auth
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("api/auth/profile")
    suspend fun getCurrentUser(): UserDto

    // Cars
    @GET("api/cars")
    suspend fun getCars(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("make") make: String? = null,
        @Query("bodyType") bodyType: String? = null,
        @Query("minPrice") minPrice: Int? = null,
        @Query("maxPrice") maxPrice: Int? = null
    ): CarsResponse

    @GET("api/cars/{id}")
    suspend fun getCarById(@Path("id") id: String): CarDto

    @GET("api/cars/search")
    suspend fun searchCars(@Query("q") query: String): CarsResponse

    // Favorites
    @GET("api/favorites")
    suspend fun getFavorites(): List<CarDto>

    @POST("api/favorites")
    suspend fun addFavorite(@Body request: FavoriteRequest): ApiResponse

    @DELETE("api/favorites/{carId}")
    suspend fun removeFavorite(@Path("carId") carId: String): ApiResponse

    // AI
    @POST("api/ai/chat")
    suspend fun chat(@Body request: AiChatRequest): AiChatResponse
}

