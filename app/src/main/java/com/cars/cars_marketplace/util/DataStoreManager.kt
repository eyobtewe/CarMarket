package com.cars.cars_marketplace.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "app_prefs")

@Singleton
class DataStoreManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) : TokenStore {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    private val ds = context.dataStore

    override suspend fun saveToken(token: String) {
        ds.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    override fun getToken(): Flow<String?> =
        ds.data.map { it[TOKEN_KEY] }

    override suspend fun clearToken() {
        ds.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }

    override suspend fun getTokenOnce(): String? = getToken().firstOrNull()
}
