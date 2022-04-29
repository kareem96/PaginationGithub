package com.kareem.appusergithub.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsMode private constructor(private val dataStore: DataStore<Preferences>) {

    private val THEME_KEY = booleanPreferencesKey("setting_mode")

    fun getThemeMode(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {


        @Volatile
        private var INSTANCE: SettingsMode? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingsMode {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsMode(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}