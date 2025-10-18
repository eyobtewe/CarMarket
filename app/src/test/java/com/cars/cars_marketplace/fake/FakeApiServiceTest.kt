package com.cars.cars_marketplace.fake

import com.cars.cars_marketplace.data.remote.dto.LoginRequest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeApiServiceTest {

    private val api = FakeApiService()

    @Test
    fun testGetCarsReturnsItems() = runBlocking {
        val response = api.getCars()
        assertTrue(response.items.isNotEmpty())
    }

    @Test
    fun testLoginReturnsToken() = runBlocking {
        val resp = api.login(LoginRequest(email = "test@example.com", password = "pass"))
        assertTrue(resp.token.isNotBlank())
    }
}

