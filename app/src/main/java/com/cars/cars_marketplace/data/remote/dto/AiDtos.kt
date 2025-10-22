package com.cars.cars_marketplace.data.remote.dto

// AI Chat DTOs
data class AiChatRequest(val message: String)

data class AiChatResponse(
    val message: String, 
    val data: AiChatData, 
    val success: Boolean
)

data class AiChatData(
    val message: String,
    val recommendedCars: List<CarDto>
)
