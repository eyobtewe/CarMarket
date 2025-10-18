package com.cars.cars_marketplace.domain.repository

import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.data.remote.dto.UserDto
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Resource<Unit>
    suspend fun register(firstName: String, lastName: String, email: String, password: String): Resource<Unit>
    suspend fun getCurrentUser(): Resource<UserDto>
}

