package com.alexser.weathernote.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Extensión del contexto para usar DataStore con el nombre "home_municipio_prefs"
private val Context.dataStore by preferencesDataStore(name = "home_municipio_prefs")

/**
 * Gestiona las preferencias locales relacionadas con el municipio favorito
 * que se muestra en la pantalla de inicio de la app.
 */
@Singleton
class HomeMunicipioPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        // Clave utilizada para guardar el ID del municipio favorito
        private val HOME_MUNICIPIO_ID = stringPreferencesKey("home_municipio_id")
    }

    /*
     * Emite el ID del municipio favorito actualmente guardado en DataStore.
     * Será null si no se ha guardado ningún municipio todavía.
     */
    val homeMunicipioId: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[HOME_MUNICIPIO_ID]
    }

    /**
     * Guarda el ID del municipio favorito en DataStore.
     * Este valor será utilizado por HomeScreen para mostrar su información.
     */
    suspend fun setHomeMunicipioId(id: String) {
        context.dataStore.edit { prefs ->
            prefs[HOME_MUNICIPIO_ID] = id
        }
    }

    /**
     * Elimina el municipio favorito guardado en DataStore.
     * Implica que ya no habrá un municipio destacado en la HomeScreen.
     */
    suspend fun clearHomeMunicipioId() {
        context.dataStore.edit { prefs ->
            prefs.remove(HOME_MUNICIPIO_ID)
        }
    }
}
