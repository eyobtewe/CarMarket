package com.cars.cars_marketplace.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CarDetail : Screen("detail/{carId}") {
        fun createRoute(carId: String) = "detail/$carId"
    }
    object Favorites : Screen("favorites")
    object Chat : Screen("chat")
}
