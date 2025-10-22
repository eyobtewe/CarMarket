package com.cars.cars_marketplace.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cars.cars_marketplace.domain.model.Car
import com.cars.cars_marketplace.presentation.common.LoadingIndicator
import com.cars.cars_marketplace.presentation.common.ErrorMessage
import com.cars.cars_marketplace.presentation.common.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onCarClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val query by viewModel.query.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize()) {
            // Twitter-like search bar
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.updateQuery(it) },
                label = { 
                    Text(
                        "Search cars...",
                        style = MaterialTheme.typography.bodyMedium
                    ) 
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateQuery("") }) {
                            Icon(
                                Icons.Filled.Clear,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(8.dp)
            )

            when (uiState) {
                is HomeUiState.Loading -> {
                    LoadingIndicator(message = "Loading cars...", modifier = Modifier.fillMaxSize())
                }
                is HomeUiState.Error -> {
                    ErrorMessage(
                        message = (uiState as HomeUiState.Error).message,
                        onRetry = { viewModel.loadCars() },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is HomeUiState.Success -> {
                    val cars = (uiState as HomeUiState.Success).cars
                    if (cars.isEmpty()) {
                        EmptyState(
                            title = "No Cars Found",
                            message = "There are no cars available at the moment. Please try again later.",
                            actionText = "Refresh",
                            onAction = { viewModel.loadCars() },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        CarsList(
                            cars = cars,
                            contentPadding = innerPadding,
                            favoriteIds = favoriteIds,
                            onClick = onCarClick,
                            onToggleFavorite = { id -> viewModel.toggleFavorite(id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CarsList(
    cars: List<Car>,
    contentPadding: PaddingValues,
    favoriteIds: Set<String>,
    onClick: (String) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = contentPadding,
                modifier = Modifier.fillMaxSize()
            ) {
        items(cars, key = { it.id }) { car ->
            val isFav = favoriteIds.contains(car.id)
            CarCard(
                car = car,
                isFavorite = isFav,
                onFavoriteClick = { onToggleFavorite(car.id) },
                onClick = { onClick(car.id) }
            )
        }
    }
}
