package com.cars.cars_marketplace.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{carId}") {
        fun createRoute(carId: String) = "detail/$carId"
    }
}

