package com.cars.cars_marketplace.data.repository

import com.cars.cars_marketplace.data.remote.dto.AiChatRequest
import com.cars.cars_marketplace.fake.FakeApiService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class AiRepositoryImplTest {

    @Test
    fun chat_returns_success_with_recommendations() = runBlocking {
        val api = FakeApiService()
        val repo = AiRepositoryImpl(api)

        val res = repo.chat(AiChatRequest("Looking for an SUV under 30000"))
        assertTrue(res is com.cars.cars_marketplace.domain.model.Resource.Success)
    }
}

