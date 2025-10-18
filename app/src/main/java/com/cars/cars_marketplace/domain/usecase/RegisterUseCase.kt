package com.cars.cars_marketplace.domain.usecase

import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(firstName: String, lastName: String, email: String, password: String): Resource<Unit> =
        repository.register(firstName, lastName, email, password)
}

