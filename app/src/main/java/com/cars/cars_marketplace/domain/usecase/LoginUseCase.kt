package com.cars.cars_marketplace.domain.usecase

import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Resource<Unit> =
        repository.login(email, password)
}

