package com.cars.cars_marketplace.di

import com.cars.cars_marketplace.data.repository.AiRepositoryImpl
import com.cars.cars_marketplace.data.repository.CarRepositoryImpl
import com.cars.cars_marketplace.data.repository.FavoriteRepositoryImpl
import com.cars.cars_marketplace.domain.repository.AiRepository
import com.cars.cars_marketplace.domain.repository.CarRepository
import com.cars.cars_marketplace.domain.repository.FavoriteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds abstract fun bindCarRepository(impl: CarRepositoryImpl): CarRepository

    @Binds abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @Binds abstract fun bindAiRepository(impl: AiRepositoryImpl): AiRepository
}
