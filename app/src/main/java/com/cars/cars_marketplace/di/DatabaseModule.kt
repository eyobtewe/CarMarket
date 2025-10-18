package com.cars.cars_marketplace.di

import android.content.Context
import androidx.room.Room
import com.cars.cars_marketplace.data.local.dao.CarDao
import com.cars.cars_marketplace.data.local.dao.FavoriteDao
import com.cars.cars_marketplace.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Suppress("DEPRECATION")
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "car_market_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCarDao(db: AppDatabase): CarDao = db.carDao()

    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()
}
