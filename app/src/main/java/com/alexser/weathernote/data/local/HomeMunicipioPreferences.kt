package com.alexser.weathernote.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "home_municipio_prefs")

@Singleton
class HomeMunicipioPreferences @Inject constructor(
    @ApplicationContext private val context: Context // ✅ Fixed: annotate context
) {
    companion object {
        private val HOME_MUNICIPIO_ID = stringPreferencesKey("home_municipio_id")
    }

    /** Read current home municipio ID as Flow<String?> */
    val homeMunicipioId: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[HOME_MUNICIPIO_ID]
    }

    /** Save municipio ID as the current home municipio */
    suspend fun setHomeMunicipioId(id: String) {
        context.dataStore.edit { prefs ->
            prefs[HOME_MUNICIPIO_ID] = id
        }
    }

    /** Optional: clear home municipio */
    suspend fun clearHomeMunicipioId() {
        context.dataStore.edit { prefs ->
            prefs.remove(HOME_MUNICIPIO_ID)
        }
    }
}
