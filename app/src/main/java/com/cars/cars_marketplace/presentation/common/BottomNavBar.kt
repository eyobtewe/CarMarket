package com.cars.cars_marketplace.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.cars.cars_marketplace.presentation.common.AnimatedIcon
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.rotate

private data class BottomNavigationItem(
        val route: String,
        val label: String,
        val icon: ImageVector
)

@Composable
fun BottomNavBar(currentRoute: String?, onNavigate: (String) -> Unit) {
    val items =
            listOf(
                    BottomNavigationItem(
                            route = "home",
                            label = "Cars",
                            icon = Icons.Filled.DirectionsCar
                    ),
                    BottomNavigationItem(
                            route = "favorites",
                            label = "Stars",
                            icon = Icons.Filled.Star
                    ),
                    BottomNavigationItem(
                            route = "chat",
                            label = "AI",
                            icon = Icons.Filled.SmartToy
                    )
            )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp
    ) {
                items.forEach { item ->
                    val isSelected = currentRoute == item.route
                    
                    NavigationBarItem(
                            selected = isSelected,
                            onClick = { onNavigate(item.route) },
                            icon = { 
                                AnimatedIcon(
                                    icon = item.icon,
                                    contentDescription = item.label,
                                    size = 26.dp,
                                    tint = if (isSelected) 
                                        MaterialTheme.colorScheme.primary 
                                    else 
                                        MaterialTheme.colorScheme.onSurfaceVariant,
                                    onClick = { onNavigate(item.route) },
                                    enableMicroInteractions = true
                                )
                            },
                label = { 
                    Text(
                        item.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (currentRoute == item.route) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            )
        }
    }
}
