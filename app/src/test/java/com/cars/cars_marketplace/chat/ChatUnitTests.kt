package com.cars.cars_marketplace.chat

import com.cars.cars_marketplace.data.remote.dto.AiChatRequest
import com.cars.cars_marketplace.data.remote.dto.AiChatResponse
import com.cars.cars_marketplace.data.remote.dto.SimpleCar
import com.cars.cars_marketplace.data.repository.AiRepositoryImpl
import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.presentation.chat.ChatViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ChatUnitTests {
    
    // Mock ApiService for chat testing
    class MockChatApiService : com.cars.cars_marketplace.data.remote.api.ApiService {
        private var shouldFail = false
        private var responseDelay = 0L
        
        fun setShouldFail(fail: Boolean) { shouldFail = fail }
        fun setResponseDelay(delay: Long) { responseDelay = delay }
        
        override suspend fun chat(request: AiChatRequest): AiChatResponse {
            if (responseDelay > 0) {
                kotlinx.coroutines.delay(responseDelay)
            }
            
            if (shouldFail) {
                throw Exception("Unauthorized: No token provided")
            }
            
            return AiChatResponse(
                message = "Here are some car recommendations based on your query: ${request.message}",
                recommendedCars = listOf(
                    SimpleCar("1", "Toyota", "Camry"),
                    SimpleCar("2", "Honda", "Civic"),
                    SimpleCar("3", "Ford", "Focus")
                )
            )
        }
        
        override suspend fun register(request: com.cars.cars_marketplace.data.remote.dto.RegisterRequest): com.cars.cars_marketplace.data.remote.dto.AuthResponse {
            throw NotImplementedError()
        }
        
        override suspend fun login(request: com.cars.cars_marketplace.data.remote.dto.LoginRequest): com.cars.cars_marketplace.data.remote.dto.AuthResponse {
            throw NotImplementedError()
        }
        
        override suspend fun getCurrentUser(): com.cars.cars_marketplace.data.remote.dto.UserDto {
            throw NotImplementedError()
        }
        
        override suspend fun getCars(page: Int, limit: Int, make: String?, bodyType: String?, minPrice: Int?, maxPrice: Int?): com.cars.cars_marketplace.data.remote.dto.CarsResponse {
            throw NotImplementedError()
        }
        
        override suspend fun getCarById(id: String): com.cars.cars_marketplace.data.remote.dto.CarDto {
            throw NotImplementedError()
        }
        
        override suspend fun searchCars(query: String): com.cars.cars_marketplace.data.remote.dto.CarsResponse {
            throw NotImplementedError()
        }
        
        override suspend fun getFavorites(): List<com.cars.cars_marketplace.data.remote.dto.CarDto> {
            throw NotImplementedError()
        }
        
        override suspend fun addFavorite(request: com.cars.cars_marketplace.data.remote.dto.FavoriteRequest): com.cars.cars_marketplace.data.remote.dto.ApiResponse {
            throw NotImplementedError()
        }
        
        override suspend fun removeFavorite(carId: String): com.cars.cars_marketplace.data.remote.dto.ApiResponse {
            throw NotImplementedError()
        }
    }
    
    private lateinit var mockApi: MockChatApiService
    private lateinit var aiRepository: AiRepositoryImpl
    
    @Before
    fun setup() {
        mockApi = MockChatApiService()
        aiRepository = AiRepositoryImpl(mockApi)
    }
    
    @Test
    fun `chat success with valid authentication`() = runBlocking {
        // Given
        val message = "Looking for a reliable car under $30000"
        val request = AiChatRequest(message)
        
        // When
        val result = aiRepository.chat(request)
        
        // Then
        assertTrue("Chat should be successful", result is Resource.Success)
        val response = (result as Resource.Success).data
        assertNotNull("Response should not be null", response)
        assertTrue("Response should contain message", response.message.isNotEmpty())
        assertEquals("Should have 3 recommended cars", 3, response.recommendedCars.size)
        assertEquals("First car should be Toyota Camry", "Toyota", response.recommendedCars[0].make)
    }
    
    @Test
    fun `chat failure without authentication`() = runBlocking {
        // Given
        mockApi.setShouldFail(true)
        val message = "Looking for a reliable car under $30000"
        val request = AiChatRequest(message)
        
        // When
        val result = aiRepository.chat(request)
        
        // Then
        assertTrue("Chat should fail", result is Resource.Error)
        val error = (result as Resource.Error)
        assertTrue("Error should contain 'Unauthorized'", 
            error.message?.contains("Unauthorized") == true)
    }
    
    @Test
    fun `chat handles empty message`() = runBlocking {
        // Given
        val message = ""
        val request = AiChatRequest(message)
        
        // When
        val result = aiRepository.chat(request)
        
        // Then
        assertTrue("Chat should be successful even with empty message", result is Resource.Success)
        val response = (result as Resource.Success).data
        assertNotNull("Response should not be null", response)
    }
    
    @Test
    fun `chat handles long message`() = runBlocking {
        // Given
        val longMessage = "I am looking for a car that is reliable, fuel efficient, has good safety ratings, " +
                "is under $25000, preferably a sedan or hatchback, automatic transmission, " +
                "good for city driving, low maintenance costs, and has good resale value. " +
                "I prefer Japanese or Korean brands but open to other suggestions."
        val request = AiChatRequest(longMessage)
        
        // When
        val result = aiRepository.chat(request)
        
        // Then
        assertTrue("Chat should handle long messages", result is Resource.Success)
        val response = (result as Resource.Success).data
        assertNotNull("Response should not be null", response)
        assertTrue("Response should contain the original message", 
            response.message.contains(longMessage))
    }
}
