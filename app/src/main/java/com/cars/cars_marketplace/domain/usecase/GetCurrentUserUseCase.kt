package com.cars.cars_marketplace.domain.usecase

import com.cars.cars_marketplace.data.remote.dto.UserDto
import com.cars.cars_marketplace.domain.model.Resource
import com.cars.cars_marketplace.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(): Resource<UserDto> = repository.getCurrentUser()
}

