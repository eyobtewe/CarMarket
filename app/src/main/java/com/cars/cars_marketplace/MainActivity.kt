package com.cars.cars_marketplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cars.cars_marketplace.presentation.home.HomeScreen
import com.cars.cars_marketplace.ui.theme.CarMarketTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarMarketTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            HomeScreen(navController = navController)
                        }
                        composable("detail/{carId}") { backStackEntry ->
                            val carId = backStackEntry.arguments?.getString("carId")
                            // Pass outer scaffold padding into the detail screen
                            DetailScreen(carId = carId, contentPadding = innerPadding)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailScreen(carId: String?, contentPadding: PaddingValues) {
    Box(modifier = Modifier.padding(contentPadding)) {
        Text(text = "Detail for car: ${carId ?: "-"}")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CarMarketTheme {
        Text(text = "Preview")
    }
}