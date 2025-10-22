package com.cars.cars_marketplace.data.remote.api

import com.cars.cars_marketplace.data.remote.dto.*
import retrofit2.http.*

interface ApiService {
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

    @GET("api/cars/{id}") suspend fun getCarById(@Path("id") id: String): CarDto

    @GET("api/cars/search") suspend fun searchCars(@Query("q") query: String): SearchResponse

    // AI
    @POST("api/ai/chat") suspend fun chat(@Body request: AiChatRequest): AiChatResponse
}
