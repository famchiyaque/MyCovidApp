package com.app.mycovidapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    val lastSearchQuery: Flow<String> = dataStore.data.map { preferences ->
        preferences[UserPreferencesKeys.LAST_SEARCH_QUERY] ?: ""
    }

    suspend fun saveLastSearchQuery(query: String) {
        dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.LAST_SEARCH_QUERY] = query
        }
    }
}

