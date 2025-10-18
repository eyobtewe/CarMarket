package com.cars.cars_marketplace.data.repository

import com.cars.cars_marketplace.data.remote.dto.LoginRequest
import com.cars.cars_marketplace.data.remote.dto.RegisterRequest
import com.cars.cars_marketplace.data.remote.dto.UserDto
import com.cars.cars_marketplace.data.remote.api.ApiService
import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.repository.AuthRepository
import com.cars.cars_marketplace.util.TokenStore
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val tokenStore: TokenStore
) : AuthRepository {

    override suspend fun login(email: String, password: String): Resource<Unit> {
        return try {
            val resp = api.login(LoginRequest(email = email, password = password))
            // save token
            tokenStore.saveToken(resp.token)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message, e)
        }
    }

    override suspend fun register(firstName: String, lastName: String, email: String, password: String): Resource<Unit> {
        return try {
            val resp = api.register(RegisterRequest(firstName = firstName, lastName = lastName, email = email, password = password))
            tokenStore.saveToken(resp.token)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message, e)
        }
    }

    override suspend fun getCurrentUser(): Resource<UserDto> {
        return try {
            val user = api.getCurrentUser()
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.message, e)
        }
    }
}
