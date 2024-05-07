package com.koreatech.kotrip_android.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class DataStoreImpl(
    private val context: Context
) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER)

    val accessToken = stringPreferencesKey(ACCESS_TOKEN)
    val refreshToken = stringPreferencesKey(REFRESH_TOKEN)

    fun getAccessToken() = context.dataStore.data.map { preferences ->
        preferences[accessToken]
    }

    fun getRefreshToken() = context.dataStore.data.map { preferences ->
        preferences[refreshToken]
    }

    suspend fun setAccessToken(token: String) {
        context.dataStore.edit { user ->
            user[accessToken] = token
        }
    }


    suspend fun setRefreshToken(token: String) {
        context.dataStore.edit { user ->
            user[refreshToken] = token
        }
    }

    suspend fun removeAccessToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(accessToken)
        }
    }

    suspend fun removeRefreshToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(refreshToken)
        }
    }

    companion object {
        const val USER = "user"
        const val ACCESS_TOKEN = "accessToken"
        const val REFRESH_TOKEN = "refreshToken"
    }
}