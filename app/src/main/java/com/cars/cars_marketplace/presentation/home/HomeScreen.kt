package com.cars.cars_marketplace.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.cars.cars_marketplace.domain.model.Car

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (uiState) {
            is HomeUiState.Loading -> {
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                }
            }
            is HomeUiState.Error -> {
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = (uiState as HomeUiState.Error).message, style = MaterialTheme.typography.bodyLarge)
                }
            }
            is HomeUiState.Success -> {
                val cars = (uiState as HomeUiState.Success).cars
                CarsList(cars = cars, contentPadding = innerPadding) { carId ->
                    navController.navigate("detail/$carId")
                }
            }
        }
    }
}

@Composable
fun CarsList(cars: List<Car>, contentPadding: PaddingValues, onClick: (String) -> Unit) {
    LazyColumn(contentPadding = contentPadding) {
        items(cars, key = { it.id }) { car ->
            CarCard(car = car, onClick = { onClick(car.id) })
        }
    }
}
