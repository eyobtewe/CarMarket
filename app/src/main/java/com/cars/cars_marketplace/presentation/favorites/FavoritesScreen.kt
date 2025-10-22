package com.cars.cars_marketplace.presentation.favorites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.presentation.home.CarCard
import com.cars.cars_marketplace.presentation.common.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onCarClick: (String) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize(), contentWindowInsets = WindowInsets.safeDrawing) { innerPadding ->
        if (favorites.isEmpty()) {
            EmptyState(
                title = "No Favorites Yet",
                message = "Start exploring cars and add them to your favorites!",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazyColumn(contentPadding = innerPadding) {
                items(favorites, key = { it.id }) { car ->
                    CarCard(
                        car = car,
                        isFavorite = true,
                        onFavoriteClick = { viewModel.toggleFavorite(car.id) },
                        onClick = { onCarClick(car.id) }
                    )
                }
            }
        }
    }
}
