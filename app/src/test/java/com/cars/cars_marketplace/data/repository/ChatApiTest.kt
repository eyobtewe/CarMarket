package com.cars.cars_marketplace.data.repository

import com.cars.cars_marketplace.data.remote.dto.AiChatRequest
import com.cars.cars_marketplace.data.remote.dto.AiChatResponse
import com.cars.cars_marketplace.data.remote.dto.SimpleCar
import com.cars.cars_marketplace.domain.model.Resource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class ChatApiTest {

    // Mock ApiService for testing
    class MockApiService : com.cars.cars_marketplace.data.remote.api.ApiService {
        private var shouldFail = false

        fun setShouldFail(fail: Boolean) {
            shouldFail = fail
        }

        override suspend fun chat(request: AiChatRequest): AiChatResponse {
            if (shouldFail) {
                throw Exception("API Error: Service unavailable")
            }
            return AiChatResponse(
                    message =
                            "Here are some car recommendations based on your query: ${request.message}",
                    recommendedCars =
                            listOf(
                                    SimpleCar("1", "Toyota", "Camry"),
                                    SimpleCar("2", "Honda", "Civic")
                            )
            )
        }

        override suspend fun getCars(
                page: Int,
                limit: Int,
                make: String?,
                bodyType: String?,
                minPrice: Int?,
                maxPrice: Int?
        ): com.cars.cars_marketplace.data.remote.dto.CarsResponse {
            throw NotImplementedError()
        }

        override suspend fun getCarById(
                id: String
        ): com.cars.cars_marketplace.data.remote.dto.CarDto {
            throw NotImplementedError()
        }

        override suspend fun searchCars(
                query: String
        ): com.cars.cars_marketplace.data.remote.dto.CarsResponse {
            throw NotImplementedError()
        }
    }

    @Test
    fun `test chat api works successfully`() = runBlocking {
        val mockApi = MockApiService()
        val aiRepository = AiRepositoryImpl(mockApi)

        // Test chat functionality
        val request = AiChatRequest("Looking for a reliable car under $30000")
        val result = aiRepository.chat(request)

        // Verify success
        assertTrue("Chat should be successful", result is Resource.Success)

        val response = (result as Resource.Success).data
        assertNotNull("Response should not be null", response)
        assertTrue("Response should contain message", response.message.isNotEmpty())
        assertEquals("Should have 2 recommended cars", 2, response.recommendedCars.size)
    }

    @Test
    fun `test chat api fails on error`() = runBlocking {
        val mockApi = MockApiService()
        mockApi.setShouldFail(true) // Simulate API error
        val aiRepository = AiRepositoryImpl(mockApi)

        // Test chat with API error
        val request = AiChatRequest("Looking for a reliable car under $30000")
        val result = aiRepository.chat(request)

        // Verify failure
        assertTrue("Chat should fail on API error", result is Resource.Error)

        val error = (result as Resource.Error)
        assertTrue(
                "Error message should contain 'API Error'",
                error.message?.contains("API Error") == true
        )
    }
}
