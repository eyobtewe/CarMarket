package com.cars.cars_marketplace.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.appDataStore by preferencesDataStore(name = "app_prefs")

@Singleton
class DataStoreManager @Inject constructor(@param:ApplicationContext private val context: Context) {

    companion object {
        private val THEME_DARK_KEY = booleanPreferencesKey("theme_dark")
        private val FAVORITES_KEY = stringSetPreferencesKey("favorites")
    }

    private val ds = context.appDataStore

    // Theme settings
    fun isDarkTheme(): Flow<Boolean> = ds.data.map { it[THEME_DARK_KEY] ?: false }

    suspend fun setDarkTheme(enabled: Boolean) {
        ds.edit { prefs -> prefs[THEME_DARK_KEY] = enabled }
    }

    // Favorites management
    fun getFavorites(): Flow<Set<String>> = ds.data.map { it[FAVORITES_KEY] ?: emptySet() }

    suspend fun addFavorite(carId: String) {
        ds.edit { prefs ->
            val currentFavorites = prefs[FAVORITES_KEY] ?: emptySet()
            prefs[FAVORITES_KEY] = currentFavorites + carId
        }
    }

    suspend fun removeFavorite(carId: String) {
        ds.edit { prefs ->
            val currentFavorites = prefs[FAVORITES_KEY] ?: emptySet()
            prefs[FAVORITES_KEY] = currentFavorites - carId
        }
    }

    suspend fun isFavorite(carId: String): Boolean {
        val favorites = getFavorites().firstOrNull() ?: emptySet()
        return favorites.contains(carId)
    }

    suspend fun clearFavorites() {
        ds.edit { prefs -> prefs.remove(FAVORITES_KEY) }
    }
}
