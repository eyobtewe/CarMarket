package com.cars.cars_marketplace.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
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
import com.cars.cars_marketplace.presentation.navigation.Screen

private data class BottomNavigationItem(
        val screen: Screen,
        val label: String,
        val icon: ImageVector
)

@Composable
fun BottomNavBar(currentRoute: String?, onNavigate: (String) -> Unit) {
    val items =
            listOf(
                    BottomNavigationItem(
                            screen = Screen.Home,
                            label = "Home",
                            icon = Icons.Filled.Home
                    ),
                    BottomNavigationItem(
                            screen = Screen.Favorites,
                            label = "Favorites",
                            icon = Icons.Filled.Favorite
                    ),
                    BottomNavigationItem(
                            screen = Screen.Chat,
                            label = "Chat",
                            icon = Icons.Filled.Chat
                    )
            )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick = { onNavigate(item.screen.route) },
                icon = { 
                    Icon(
                        imageVector = item.icon, 
                        contentDescription = item.label,
                        tint = if (currentRoute == item.screen.route) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    ) 
                },
                label = { 
                    Text(
                        item.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (currentRoute == item.screen.route) 
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
