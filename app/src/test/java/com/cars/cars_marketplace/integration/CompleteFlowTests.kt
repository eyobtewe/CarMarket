package com.cars.cars_marketplace.integration

import com.cars.cars_marketplace.data.remote.dto.AiChatRequest
import com.cars.cars_marketplace.data.remote.dto.AiChatResponse
import com.cars.cars_marketplace.data.remote.dto.CarDto
import com.cars.cars_marketplace.data.remote.dto.CarsData
import com.cars.cars_marketplace.data.remote.dto.CarsResponse
import com.cars.cars_marketplace.data.remote.dto.SimpleCar
import com.cars.cars_marketplace.data.repository.AiRepositoryImpl
import com.cars.cars_marketplace.domain.model.Resource
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CompleteFlowTests {

    // Mock ApiService for integration testing
    class MockApiService : com.cars.cars_marketplace.data.remote.api.ApiService {

        override suspend fun getCars(
                page: Int,
                limit: Int,
                make: String?,
                bodyType: String?,
                minPrice: Int?,
                maxPrice: Int?
        ): CarsResponse {
            return CarsResponse(
                    message = "Success",
                    data =
                            CarsData(
                                    count = 2,
                                    rows =
                                            listOf(
                                                    CarDto(
                                                            "1",
                                                            "Toyota",
                                                            "Camry",
                                                            2020,
                                                            25000.0,
                                                            "Sedan",
                                                            50000,
                                                            "White",
                                                            "Great car",
                                                            emptyList()
                                                    ),
                                                    CarDto(
                                                            "2",
                                                            "Honda",
                                                            "Civic",
                                                            2021,
                                                            22000.0,
                                                            "Sedan",
                                                            30000,
                                                            "Blue",
                                                            "Excellent condition",
                                                            emptyList()
                                                    )
                                            )
                            ),
                    success = true
            )
        }

        override suspend fun getCarById(id: String): CarDto {
            return CarDto(
                    id,
                    "Toyota",
                    "Camry",
                    2020,
                    25000.0,
                    "Sedan",
                    50000,
                    "White",
                    "Great car",
                    emptyList()
            )
        }

        override suspend fun searchCars(query: String): CarsResponse {
            return CarsResponse(
                    message = "Search results",
                    data =
                            CarsData(
                                    count = 1,
                                    rows =
                                            listOf(
                                                    CarDto(
                                                            "1",
                                                            "Toyota",
                                                            "Camry",
                                                            2020,
                                                            25000.0,
                                                            "Sedan",
                                                            50000,
                                                            "White",
                                                            "Great car",
                                                            emptyList()
                                                    )
                                            )
                            ),
                    success = true
            )
        }

        override suspend fun chat(request: AiChatRequest): AiChatResponse {
            return AiChatResponse(
                    message = "Based on your request, I recommend the Toyota Camry",
                    recommendedCars = listOf(SimpleCar("1", "Toyota", "Camry"))
            )
        }
    }

    private lateinit var mockApiService: MockApiService
    private lateinit var aiRepository: AiRepositoryImpl

    @Before
    fun setup() {
        mockApiService = MockApiService()
        aiRepository = AiRepositoryImpl(mockApiService)
    }

    @Test
    fun `test car browsing flow`() = runBlocking {
        // Test getting cars
        val carsResponse = mockApiService.getCars()
        assertTrue("Should return success", carsResponse.success)
        assertEquals("Should have 2 cars", 2, carsResponse.data.count)
        assertEquals("First car should be Toyota Camry", "Toyota", carsResponse.data.rows[0].make)
    }

    @Test
    fun `test car detail flow`() = runBlocking {
        // Test getting car by ID
        val car = mockApiService.getCarById("1")
        assertEquals("Car ID should match", "1", car.id)
        assertEquals("Car make should be Toyota", "Toyota", car.make)
    }

    @Test
    fun `test car search flow`() = runBlocking {
        // Test searching cars
        val searchResponse = mockApiService.searchCars("Toyota")
        assertTrue("Search should return success", searchResponse.success)
        assertEquals("Should find 1 car", 1, searchResponse.data.count)
    }

    @Test
    fun `test AI chat flow`() = runBlocking {
        // Test AI chat functionality
        val chatRequest = AiChatRequest("I'm looking for a reliable sedan")
        val chatResponse = mockApiService.chat(chatRequest)

        assertNotNull("Response should not be null", chatResponse.message)
        assertTrue("Should have recommendations", chatResponse.recommendedCars.isNotEmpty())
        assertEquals(
                "Should recommend Toyota Camry",
                "Toyota",
                chatResponse.recommendedCars[0].make
        )
    }

    @Test
    fun `test AI repository integration`() = runBlocking {
        // Test AI repository with mock service
        val result = aiRepository.chat("I need a reliable car")

        assertTrue("Should return success", result is Resource.Success)
        if (result is Resource.Success) {
            val response = result.data
            assertNotNull("Response should not be null", response.message)
            assertTrue("Should have recommendations", response.recommendedCars.isNotEmpty())
        }
    }
}
