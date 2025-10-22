package com.cars.cars_marketplace.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavBackStackEntry
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val json = Json { ignoreUnknownKeys = true }

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
    val route = arguments?.getString("route") ?: ""
    return json.decodeFromString(route)
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
