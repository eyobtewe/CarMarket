package com.cars.cars_marketplace.domain.usecase

import com.cars.cars_marketplace.data.remote.dto.AiChatRequest
import com.cars.cars_marketplace.data.remote.dto.AiChatResponse
import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.repository.AiRepository
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(
    private val repository: AiRepository
) {
    suspend operator fun invoke(message: String): Resource<AiChatResponse> {
        val request = AiChatRequest(message = message)
        return repository.chat(request)
    }
}
