package com.cars.cars_marketplace.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object Home : Screen()
    
    @Serializable
    data class CarDetail(val carId: String) : Screen()
    
    @Serializable
    object Favorites : Screen()
    
    @Serializable
    object Chat : Screen()
}
