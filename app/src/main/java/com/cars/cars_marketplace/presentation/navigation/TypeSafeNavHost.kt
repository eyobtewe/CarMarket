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

/**
 * Type-safe NavHost wrapper for Navigation 3
 */
@Composable
fun TypeSafeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onCarClick = { carId ->
                    navController.navigateTo(Screen.CarDetail(carId))
                }
            )
        }
        
        composable<Screen.CarDetail> { backStackEntry ->
            val carId = backStackEntry.toRoute<Screen.CarDetail>().carId
            CarDetailScreen(
                carId = carId, 
                onBack = { navController.popBackStack() }
            )
        }
        
        composable<Screen.Favorites> {
            FavoritesScreen(
                onCarClick = { carId ->
                    navController.navigateTo(Screen.CarDetail(carId))
                }
            )
        }
        
        composable<Screen.Chat> { 
            ChatScreen(navController = navController) 
        }
    }
}
