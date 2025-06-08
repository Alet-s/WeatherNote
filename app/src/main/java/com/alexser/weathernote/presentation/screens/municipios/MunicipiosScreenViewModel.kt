package com.alexser.weathernote.presentation.screens.municipios

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.firebase.MunicipioSyncService
import com.alexser.weathernote.data.local.HomeMunicipioPreferences
import com.alexser.weathernote.data.local.SnapshotPreferences
import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem
import com.alexser.weathernote.domain.model.DailyForecast
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.usecase.*
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import toHourlyForecastFullItems
import java.io.InputStreamReader
import javax.inject.Inject

/**
 * ViewModel encargado de manejar la lógica y estado de la pantalla de municipios.
 * Gestiona la lista de municipios guardados, sincronización con Firestore,
 * obtención de predicciones meteorológicas y operaciones relacionadas.
 *
 * @property getSavedMunicipiosUseCase Caso de uso para obtener municipios guardados.
 * @property addMunicipioUseCase Caso de uso para añadir un municipio.
 * @property removeMunicipioUseCase Caso de uso para eliminar un municipio.
 * @property getBasicWeatherForecastUseCase Caso de uso para obtener pronóstico básico.
 * @property findMunicipioByNameUseCase Caso de uso para buscar municipio por nombre.
 * @property syncService Servicio para sincronizar municipios con Firebase Firestore.
 * @property getHourlyForecastUseCase Caso de uso para obtener pronóstico horario.
 * @property homeMunicipioPreferences Preferencias para almacenar municipio principal.
 * @property deleteSnapshotsByMunicipioUseCase Caso de uso para eliminar snapshots asociados a un municipio.
 * @property snapshotPreferences Preferencias relacionadas con snapshots.
 * @property getDailyForecastUseCase Caso de uso para obtener pronóstico diario.
 * @property getSnapshotByReportIdUseCase Caso de uso para obtener un snapshot por reportId.
 * @property saveSnapshotReportUseCase Caso de uso para guardar un reporte snapshot.
 */
@HiltViewModel
class MunicipiosScreenViewModel @Inject constructor(
    private val getSavedMunicipiosUseCase: GetSavedMunicipiosUseCase,
    private val addMunicipioUseCase: AddMunicipioUseCase,
    private val removeMunicipioUseCase: RemoveMunicipioUseCase,
    private val getBasicWeatherForecastUseCase: GetBasicWeatherForecastUseCase,
    private val findMunicipioByNameUseCase: FindMunicipioByNameUseCase,
    private val syncService: MunicipioSyncService,
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase,
    private val homeMunicipioPreferences: HomeMunicipioPreferences,
    private val deleteSnapshotsByMunicipioUseCase: DeleteSnapshotsByMunicipioUseCase,
    private val snapshotPreferences: SnapshotPreferences,
    private val getDailyForecastUseCase: GetDailyForecastUseCase,
    private val getSnapshotByReportIdUseCase: GetSnapshotByReportIdUseCase,
    private val saveSnapshotReportUseCase: SaveSnapshotReportUseCase
) : ViewModel() {

    /** Estado que contiene la lista de municipios guardados localmente. */
    private val _municipios = MutableStateFlow<List<SavedMunicipio>>(emptyList())

    /** Flujo inmutable para observar la lista de municipios. */
    val municipios: StateFlow<List<SavedMunicipio>> = _municipios

    /**
     * Mapa que asocia cada municipio con su estado UI (loading, éxito o error)
     * para mostrar el estado de la carga de snapshots.
     */
    private val _snapshotMunicipioUiStates =
        MutableStateFlow<Map<String, SnapshotMunicipioUiState>>(emptyMap())

    /** Flujo inmutable con estados UI para snapshots por municipio. */
    val snapshotMunicipioUiStates: StateFlow<Map<String, SnapshotMunicipioUiState>> =
        _snapshotMunicipioUiStates

    /** Estado que indica si la sincronización con Firestore fue exitosa (true),
     * fallida (false) o null si no se ha hecho. */
    private val _syncSuccess = MutableStateFlow<Boolean?>(null)
    val syncSuccess: StateFlow<Boolean?> = _syncSuccess

    /** Lista de sugerencias de nombres de municipios para autocompletar.
     * Sin implementación actual. */
    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> get() = _suggestions

    /**
     * Mapa con la predicción horaria completa para cada municipio,
     * donde la clave es el ID del municipio y el valor es la lista de pronósticos horarios.
     */
    private val _hourlyFullForecasts =
        MutableStateFlow<Map<String, List<HourlyForecastFullItem>>>(emptyMap())
    val hourlyFullForecasts: StateFlow<Map<String, List<HourlyForecastFullItem>>> =
        _hourlyFullForecasts

    /**
     * Flujo que contiene el ID del municipio configurado como principal (home),
     * obtenido desde las preferencias locales.
     */
    val homeMunicipioId = homeMunicipioPreferences.homeMunicipioId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Estado para mostrar mensajes en un Snackbar. */
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    /** Estado para mostrar mensajes de confirmación al establecer municipio principal. */
    private val _homeConfirmationMessage = MutableStateFlow<String?>(null)
    val homeConfirmationMessage: StateFlow<String?> = _homeConfirmationMessage

    init {
        loadMunicipiosAndSnapshots()
    }

    /**
     * Carga los municipios guardados localmente y sincroniza con Firestore.
     * También carga los snapshots para cada municipio nuevo detectado.
     */
    private fun loadMunicipiosAndSnapshots() {
        viewModelScope.launch {
            try {
                val remoteMunicipios = syncService.downloadRemoteMunicipios()
                val current = getSavedMunicipiosUseCase().first()
                val currentIds = current.map { it.id }.toSet()

                remoteMunicipios.forEach { municipio ->
                    if (municipio.id !in currentIds) {
                        addMunicipioUseCase(municipio)
                    }
                }
            } catch (e: Exception) {
                _syncSuccess.value = false
            }

            getSavedMunicipiosUseCase().collect { savedList ->
                _municipios.value = savedList
                savedList.forEach { municipio ->
                    if (!_snapshotMunicipioUiStates.value.containsKey(municipio.id)) {
                        fetchSnapshot(municipio.id)
                    }
                }
            }
        }
    }

    /**
     * Obtiene el snapshot (pronóstico básico) para un municipio dado y actualiza el estado UI.
     *
     * @param municipioId ID del municipio para obtener el snapshot.
     */
    fun fetchSnapshot(municipioId: String) {
        viewModelScope.launch {
            _snapshotMunicipioUiStates.update { it + (municipioId to SnapshotMunicipioUiState.Loading) }

            try {
                val snapshot = retryIO { getBasicWeatherForecastUseCase(municipioId).getOrThrow() }
                _snapshotMunicipioUiStates.update {
                    it + (municipioId to SnapshotMunicipioUiState.Success(
                        snapshot
                    ))
                }
            } catch (e: Exception) {
                _snapshotMunicipioUiStates.update {
                    it + (municipioId to SnapshotMunicipioUiState.Error(
                        e.message ?: "Error al cargar datos"
                    ))
                }
            }
        }
    }

    /**
     * Añade un nuevo municipio buscándolo por nombre.
     * Si no existe en la lista, se añade y se obtiene su snapshot.
     *
     * @param name Nombre del municipio a añadir.
     */
    fun addMunicipioByName(name: String) {
        viewModelScope.launch {
            val id = findMunicipioByNameUseCase(name)
            if (id != null && _municipios.value.none { it.id == id }) {
                val newMunicipio = SavedMunicipio(id = id, nombre = name)
                addMunicipioUseCase(newMunicipio)
                fetchSnapshot(id)
                syncMunicipiosToFirestore()
            }
        }
    }

    /**
     * Elimina un municipio y opcionalmente sus snapshots asociados.
     * Actualiza el estado local y sincroniza los cambios con Firestore.
     *
     * @param id ID del municipio a eliminar.
     * @param deleteSnapshots Si es true, también elimina los snapshots asociados.
     */
    fun removeMunicipioWithOption(id: String, deleteSnapshots: Boolean) {
        viewModelScope.launch {
            val toRemove = _municipios.value.find { it.id == id } ?: return@launch
            removeMunicipioUseCase(toRemove)

            if (deleteSnapshots) {
                deleteSnapshotsByMunicipioUseCase(toRemove.id)
                snapshotPreferences.removeRetentionForMunicipio(toRemove.id)
            }

            _municipios.update { it.filterNot { it.id == id } }
            _snapshotMunicipioUiStates.update { it - id }

            val msg = if (deleteSnapshots) {
                "Municipio y snapshots eliminados"
            } else {
                "Municipio eliminados (snapshots conservados)"
            }

            _snackbarMessage.value = msg
            syncMunicipiosToFirestore()
        }
    }

    /**
     * Establece un municipio como el principal y guarda la preferencia.
     * También muestra un mensaje de confirmación.
     *
     * @param id ID del municipio a configurar como principal.
     */
    fun setHomeMunicipio(id: String) {
        viewModelScope.launch {
            homeMunicipioPreferences.setHomeMunicipioId(id)
            val name = _municipios.value.find { it.id == id }?.nombre
            _homeConfirmationMessage.value = "${name ?: "Municipio"} configurado como principal"
        }
    }

    /** Limpia el mensaje del Snackbar. */
    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }

    /** Limpia el mensaje de confirmación de municipio principal. */
    fun clearHomeConfirmationMessage() {
        _homeConfirmationMessage.value = null
    }

    /**
     * Sincroniza la lista de municipios actuales a Firestore.
     * Actualiza el estado de éxito o fallo en la sincronización.
     */
    fun syncMunicipiosToFirestore() {
        viewModelScope.launch {
            try {
                syncService.uploadMunicipios(_municipios.value)
                _syncSuccess.value = true
            } catch (e: Exception) {
                _syncSuccess.value = false
            }
        }
    }

    /**
     * Recarga la lista de municipios desde Firestore y actualiza la lista local,
     * además de solicitar los snapshots para los nuevos municipios.
     */
    fun reloadFromFirestore() {
        viewModelScope.launch {
            try {
                val remoteMunicipios = syncService.downloadRemoteMunicipios()
                val current = _municipios.value
                val merged = (current + remoteMunicipios).distinctBy { it.id }

                _municipios.value = merged
                remoteMunicipios.forEach { municipio ->
                    fetchSnapshot(municipio.id)
                }
            } catch (e: Exception) {
                _syncSuccess.value = false
            }
        }
    }

    /**
     * Obtiene la predicción meteorológica por horas para un municipio,
     * transformando los DTO a objetos de UI y actualizando el estado.
     *
     * @param municipioId ID del municipio para obtener la predicción horaria.
     */
    fun fetchHourlyForecast(municipioId: String) {
        viewModelScope.launch {
            try {
                val rawDtos = getHourlyForecastUseCase(municipioId)
                val items = rawDtos.flatMap { it.toHourlyForecastFullItems() }
                _hourlyFullForecasts.update { it + (municipioId to items) }
            } catch (e: Exception) {
                // Optional logging
            }
        }
    }

    /** Mapa con la predicción diaria por municipio. */
    private val _dailyForecasts = MutableStateFlow<Map<String, List<DailyForecast>>>(emptyMap())
    val dailyForecasts: StateFlow<Map<String, List<DailyForecast>>> = _dailyForecasts

    /**
     * Obtiene la predicción diaria para un municipio y actualiza el estado.
     *
     * @param municipioId ID del municipio para obtener la predicción diaria.
     */
    fun fetchDailyForecast(municipioId: String) {
        viewModelScope.launch {
            try {
                val result = getDailyForecastUseCase(municipioId)
                _dailyForecasts.update { it + (municipioId to result) }
            } catch (_: Exception) {
            }
        }
    }

    /**
     * Ejecuta un bloque de código con reintentos en caso de fallo,
     * con un retardo configurable entre intentos.
     *
     * @param times Número de intentos.
     * @param delayMillis Retardo en milisegundos entre intentos.
     * @param block Bloque suspendido a ejecutar.
     * @return Resultado del bloque si es exitoso.
     */
    private suspend fun <T> retryIO(
        times: Int = 3,
        delayMillis: Long = 1000,
        block: suspend () -> T
    ): T {
        repeat(times - 1) {
            try {
                return block()
            } catch (_: Exception) {
                delay(delayMillis)
            }
        }
        return block()
    }

    /**
     * Importa snapshots desde un archivo JSON dado su URI.
     * Realiza deduplicación por reportId y guarda solo los nuevos snapshots.
     * Muestra un mensaje con el resumen de la operación.
     *
     * @param context Contexto para abrir el InputStream.
     * @param uri URI del archivo JSON a importar.
     */
    fun importSnapshotFromUri(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val reader = InputStreamReader(inputStream)
                val gson = Gson()
                // Parsea como array
                val snapshots: List<SnapshotReport> = gson.fromJson(
                    reader,
                    Array<SnapshotReport>::class.java
                )?.toList() ?: emptyList()
                inputStream?.close()

                var imported = 0
                var skipped = 0

                for (snapshot in snapshots) {
                    val exists = getSnapshotByReportIdUseCase(snapshot.reportId) != null
                    if (!exists) {
                        saveSnapshotReportUseCase(snapshot)
                        imported++
                    } else {
                        skipped++
                    }
                }

                loadMunicipiosAndSnapshots()

                _snackbarMessage.value = "Importados: $imported, Omitidos: $skipped"
            } catch (e: Exception) {
                _snackbarMessage.value = "Error al importar: ${e.message}"
            }
        }
    }

}
