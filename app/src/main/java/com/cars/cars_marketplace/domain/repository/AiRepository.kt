package com.cars.cars_marketplace.domain.repository

import com.cars.cars_marketplace.data.remote.dto.AiChatRequest
import com.cars.cars_marketplace.data.remote.dto.AiChatResponse
import com.cars.cars_marketplace.domain.model.Resource

interface AiRepository {
    suspend fun chat(request: AiChatRequest): Resource<AiChatResponse>
}

