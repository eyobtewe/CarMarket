package com.cars.cars_marketplace.data.repository

import com.cars.cars_marketplace.data.remote.api.ApiService
import com.cars.cars_marketplace.data.remote.dto.AiChatRequest
import com.cars.cars_marketplace.data.remote.dto.AiChatResponse
import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.repository.AiRepository
import javax.inject.Inject

class AiRepositoryImpl @Inject constructor(
    private val api: ApiService
) : AiRepository {

    override suspend fun chat(request: AiChatRequest): Resource<AiChatResponse> {
        return try {
            val resp = api.chat(request)
            Resource.Success(resp)
        } catch (e: Exception) {
            Resource.Error(e.message, e)
        }
    }
}

