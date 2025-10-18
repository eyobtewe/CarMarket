package com.cars.cars_marketplace.di

import com.cars.cars_marketplace.util.DataStoreManager
import com.cars.cars_marketplace.util.TokenStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenModule {

    @Binds
    @Singleton
    abstract fun bindTokenStore(dataStoreManager: DataStoreManager): TokenStore
}

