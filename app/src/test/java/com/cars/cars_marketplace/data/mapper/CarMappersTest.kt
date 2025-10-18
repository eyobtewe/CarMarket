package com.cars.cars_marketplace.data.mapper

import com.cars.cars_marketplace.data.remote.dto.CarDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CarMappersTest {

    @Test
    fun testCarDtoEntityDomainMappingPreservesKeyFields() = runBlocking {
        val dto = CarDto(
            id = "id-123",
            make = "Toyota",
            model = "Corolla",
            year = 2020,
            price = 20000.0,
            bodyType = "sedan",
            mileage = 15000,
            color = "Blue",
            description = "Nice car",
            images = listOf("img1.png", "img2.png")
        )

        val entity = dto.toEntity()
        val domain = entity.toDomain()

        assertEquals(dto.id, domain.id)
        assertEquals(dto.make, domain.make)
        assertEquals(dto.model, domain.model)
        assertEquals(dto.year, domain.year)
        assertEquals(dto.price, domain.price, 0.0)
        assertEquals(dto.images.size, domain.images.size)
        assertTrue(domain.images.contains("img1.png"))
    }
}
