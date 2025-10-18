package com.cars.cars_marketplace.util

import kotlinx.coroutines.flow.Flow

interface TokenStore {
    suspend fun saveToken(token: String)
    fun getToken(): Flow<String?>
    suspend fun clearToken()
    suspend fun getTokenOnce(): String?
}
