package com.cars.cars_marketplace.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cars.cars_marketplace.presentation.chat.ChatScreen
import com.cars.cars_marketplace.presentation.detail.CarDetailScreen
import com.cars.cars_marketplace.presentation.favorites.FavoritesScreen
import com.cars.cars_marketplace.presentation.home.HomeScreen

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                    onCarClick = { carId ->
                        navController.navigate(Screen.CarDetail.createRoute(carId))
                    }
            )
        }
        composable(Screen.CarDetail.route) { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId")
            CarDetailScreen(carId = carId, onBack = { navController.popBackStack() })
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                    onCarClick = { carId ->
                        navController.navigate(Screen.CarDetail.createRoute(carId))
                    }
            )
        }
        composable(Screen.Chat.route) { ChatScreen(navController = navController) }
    }
}
