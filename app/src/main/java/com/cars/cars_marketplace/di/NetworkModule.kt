package com.cars.cars_marketplace.di

import com.cars.cars_marketplace.data.remote.api.ApiService
import com.cars.cars_marketplace.data.remote.interceptor.AuthInterceptor
import com.cars.cars_marketplace.fake.FakeApiService
import com.cars.cars_marketplace.util.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Named("BASE_URL")
    fun provideBaseUrl(): String = "http://localhost:3000"

    @Provides
    @Singleton
    fun provideAuthInterceptor(dataStoreManager: DataStoreManager): AuthInterceptor =
        AuthInterceptor(dataStoreManager)

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@Named("BASE_URL") baseUrl: String, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // Provide an easy switch for using the FakeApiService in development.
    // Change this provider to `false` when you want to connect to the real backend.
    @Suppress("unused")
    @Provides
    @Singleton
    @Named("USE_FAKE")
    fun provideUseFakeFlag(): Boolean = true

    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit,
        @Named("USE_FAKE") useFake: Boolean
    ): ApiService {
        return if (useFake) {
            // Use in-memory fake implementation for local development & testing
            FakeApiService()
        } else {
            retrofit.create(ApiService::class.java)
        }
    }
}
