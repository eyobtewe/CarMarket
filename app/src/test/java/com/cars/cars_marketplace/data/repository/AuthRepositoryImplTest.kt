package com.cars.cars_marketplace.data.repository

import com.cars.cars_marketplace.fake.FakeApiService
import com.cars.cars_marketplace.util.TokenStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeTokenStore : TokenStore {
    private var token: String? = null
    override suspend fun saveToken(token: String) { this.token = token }
    override fun getToken(): Flow<String?> = flowOf(token)
    override suspend fun clearToken() { token = null }
    override suspend fun getTokenOnce(): String? = token
    fun current(): String? = token
}

class AuthRepositoryImplTest {

    @Test
    fun login_saves_token() = runBlocking {
        val api = FakeApiService()
        val tokenStore = FakeTokenStore()
        val repo = AuthRepositoryImpl(api, tokenStore)

        val res = repo.login("test@example.com", "password")
        assertTrue(res is com.cars.cars_marketplace.domain.model.Resource.Success)

        val saved = tokenStore.current()
        assertNotNull("Token should be saved after login", saved)
        assertTrue(saved!!.startsWith("fake_token_"))
    }
}
