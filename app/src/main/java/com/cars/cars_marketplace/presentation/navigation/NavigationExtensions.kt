package com.cars.cars_marketplace.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavBackStackEntry

/**
 * Navigate to a screen with type-safe arguments
 */
fun NavController.navigateTo(screen: Screen) {
    when (screen) {
        is Screen.Home -> navigate(Screen.Home)
        is Screen.CarDetail -> navigate(Screen.CarDetail(screen.carId))
        is Screen.Favorites -> navigate(Screen.Favorites)
        is Screen.Chat -> navigate(Screen.Chat)
    }
}

/**
 * Get the current route as a Screen object
 */
inline fun <reified T : Screen> NavBackStackEntry.toRoute(): T {
    return when (T::class) {
        Screen.CarDetail::class -> {
            val carId = arguments?.getString("carId") ?: ""
            Screen.CarDetail(carId) as T
        }
        Screen.Home::class -> Screen.Home as T
        Screen.Favorites::class -> Screen.Favorites as T
        Screen.Chat::class -> Screen.Chat as T
        else -> throw IllegalArgumentException("Unknown screen type: ${T::class}")
    }
}

/**
 * Navigate back to a specific screen
 */
fun NavController.navigateBackTo(screen: Screen) {
    popBackStack(
        route = when (screen) {
            is Screen.Home -> "home"
            is Screen.CarDetail -> "detail/${screen.carId}"
            is Screen.Favorites -> "favorites"
            is Screen.Chat -> "chat"
        },
        inclusive = false
    )
}
