package com.cars.cars_marketplace.di

import com.cars.cars_marketplace.data.local.dao.CarDao
import com.cars.cars_marketplace.data.local.dao.FavoriteDao
import com.cars.cars_marketplace.data.remote.api.ApiService
import com.cars.cars_marketplace.data.repository.AiRepositoryImpl
import com.cars.cars_marketplace.data.repository.AuthRepositoryImpl
import com.cars.cars_marketplace.data.repository.CarRepositoryImpl
import com.cars.cars_marketplace.data.repository.FavoriteRepositoryImpl
import com.cars.cars_marketplace.domain.repository.AiRepository
import com.cars.cars_marketplace.domain.repository.AuthRepository
import com.cars.cars_marketplace.domain.repository.CarRepository
import com.cars.cars_marketplace.domain.repository.FavoriteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCarRepository(api: ApiService, carDao: CarDao): CarRepository {
        return CarRepositoryImpl(api, carDao)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(api: ApiService, favoriteDao: FavoriteDao, carDao: CarDao): FavoriteRepository {
        return FavoriteRepositoryImpl(api, favoriteDao, carDao)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: ApiService, dataStoreManager: com.cars.cars_marketplace.util.DataStoreManager): AuthRepository {
        return AuthRepositoryImpl(api, dataStoreManager)
    }

    @Provides
    @Singleton
    fun provideAiRepository(api: ApiService): AiRepository {
        return AiRepositoryImpl(api)
    }
}
