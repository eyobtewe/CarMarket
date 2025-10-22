package com.cars.cars_marketplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cars.cars_marketplace.presentation.common.AppTopBar
import com.cars.cars_marketplace.presentation.common.BottomNavBar
import com.cars.cars_marketplace.presentation.navigation.AppNavHost
import com.cars.cars_marketplace.presentation.navigation.Screen
import com.cars.cars_marketplace.presentation.theme.ThemeViewModel
import com.cars.cars_marketplace.ui.theme.CarMarketTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            CarMarketTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                        topBar = {
                            if (currentRoute in
                                            listOf(
                                                    Screen.Home.route,
                                                    Screen.Favorites.route,
                                                    Screen.Chat.route
                                            )
                            ) {
                                AppTopBar()
                            }
                        },
                        bottomBar = {
                            if (currentRoute in
                                            listOf(
                                                    Screen.Home.route,
                                                    Screen.Favorites.route,
                                                    Screen.Chat.route
                                            )
                            ) {
                                BottomNavBar(
                                        currentRoute = currentRoute,
                                        onNavigate = {
                                            navController.navigate(it) {
                                                launchSingleTop = true
                                                restoreState = true
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                            }
                                        }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                        contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    AppNavHost(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CarMarketTheme { Text(text = "Preview") }
}
