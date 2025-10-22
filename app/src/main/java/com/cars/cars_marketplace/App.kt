package com.cars.cars_marketplace

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cars.cars_marketplace.presentation.common.AppTopBar
import com.cars.cars_marketplace.presentation.common.BottomNavBar
import com.cars.cars_marketplace.presentation.navigation.Screen
import com.cars.cars_marketplace.presentation.navigation.TypeSafeNavHost
import com.cars.cars_marketplace.presentation.navigation.navigateTo
import com.cars.cars_marketplace.presentation.theme.ThemeViewModel
import com.cars.cars_marketplace.ui.theme.CarMarketTheme


@Composable
 fun App() {
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    CarMarketTheme(darkTheme = isDarkTheme) {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Check if we're on a main screen (Home, Favorites, or Chat)
        val isMainScreen = currentRoute?.contains("Screen.Home") == true ||
                currentRoute?.contains("Screen.Favorites") == true ||
                currentRoute?.contains("Screen.Chat") == true

        Scaffold(
            topBar = {
                if (isMainScreen) {
                    AppTopBar()
                }
            },
            bottomBar = {
                if (isMainScreen) {
                    BottomNavBar(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            when (route) {
                                "home" -> navController.navigateTo(Screen.Home)
                                "favorites" -> navController.navigateTo(Screen.Favorites)
                                "chat" -> navController.navigateTo(Screen.Chat)
                            }
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing
        ) { innerPadding ->
            TypeSafeNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
