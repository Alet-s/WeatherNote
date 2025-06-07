package com.alexser.weathernote.domain.model

import com.alexser.weathernote.R

/**
 * Representa las diferentes condiciones meteorológicas con su código,
 * descripción y recurso de icono asociado.
 *
 * Cada objeto representa una condición concreta.
 *
 * @property codigo Código identificador de la condición.
 * @property descripcion Descripción legible de la condición.
 * @property iconoRes Recurso drawable del icono que representa la condición.
 */
sealed class CondicionMeteorologica(
    val codigo: String,
    val descripcion: String,
    val iconoRes: Int
) {
    object Despejado : CondicionMeteorologica("11", "Despejado", R.drawable.wi_moon_new)
    object PocoNuboso :
        CondicionMeteorologica("12", "Poco nuboso", R.drawable.wi_night_partly_cloudy)

    object Nuboso : CondicionMeteorologica("13", "Nuboso", R.drawable.wi_night_cloudy)
    object MuyNuboso : CondicionMeteorologica("14", "Muy nuboso", R.drawable.wi_cloudy)
    object Cubierto : CondicionMeteorologica("15", "Cubierto", R.drawable.wi_cloud)
    object NubesAltas :
        CondicionMeteorologica("16", "Nubes altas", R.drawable.wi_night_partly_cloudy)

    object IntervalosConLluvia :
        CondicionMeteorologica("17", "Intervalos nubosos con lluvia", R.drawable.wi_night_rain_mix)

    object NubosoConLluvia :
        CondicionMeteorologica("18", "Nuboso con lluvia", R.drawable.wi_night_rain_mix)

    object MuyNubosoConLluvia :
        CondicionMeteorologica("19", "Muy nuboso con lluvia", R.drawable.wi_rain)

    object CubiertoConLluvia :
        CondicionMeteorologica("20", "Cubierto con lluvia", R.drawable.wi_rain)

    object Tormenta :
        CondicionMeteorologica("21", "Cubierto con tormenta", R.drawable.wi_night_thunderstorm)

    object Nieblas : CondicionMeteorologica("22", "Nieblas", R.drawable.wi_windy)
    object NieblaDensa : CondicionMeteorologica("23", "Niebla densa", R.drawable.wi)
    object LluviaDebil : CondicionMeteorologica("24", "Lluvia débil", R.drawable.wi_raindrops)
    object LluviaModerada :
        CondicionMeteorologica("25", "Lluvia moderada", R.drawable.wi_night_rain_mix)

    object LluviaFuerte : CondicionMeteorologica("26", "Lluvia fuerte", R.drawable.wi_rain)
    object Chubascos : CondicionMeteorologica("27", "Chubascos", R.drawable.wi_night_rain_mix)
    object ChubascosFuertes :
        CondicionMeteorologica("28", "Chubascos fuertes", R.drawable.wi_night_rain)

    object Nieve : CondicionMeteorologica("29", "Nieve", R.drawable.wi_snow)
    object NieveDebil : CondicionMeteorologica("30", "Nieve débil", R.drawable.wi_night_snow_wind)
    object Granizo : CondicionMeteorologica("31", "Granizo", R.drawable.wi_hail)
    object TormentaElectrica :
        CondicionMeteorologica("32", "Tormenta", R.drawable.wi_night_thunderstorm)

    /**
     * Devuelve la instancia de [CondicionMeteorologica] que corresponde
     * a la descripción textual proporcionada.
     *
     * @param descripcion Descripción textual de la condición meteorológica.
     * @return Instancia correspondiente de [CondicionMeteorologica].
     *         Por defecto devuelve [Despejado] si no hay coincidencia.
     */
    companion object {
        fun fromDescripcion(descripcion: String): CondicionMeteorologica {
            return when (descripcion.trim().lowercase()) {
                "despejado" -> Despejado
                "poco nuboso" -> PocoNuboso
                "nuboso" -> Nuboso
                "muy nuboso" -> MuyNuboso
                "cubierto" -> Cubierto
                "nubes altas" -> NubesAltas
                "nieblas" -> Nieblas
                "niebla densa" -> NieblaDensa
                "granizo" -> Granizo
                "tormenta" -> TormentaElectrica
                "cubierto con tormenta" -> Tormenta
                "cubierto con lluvia" -> CubiertoConLluvia
                "muy nuboso con lluvia" -> MuyNubosoConLluvia
                "nuboso con lluvia" -> NubosoConLluvia
                "intervalos nubosos" -> Nuboso
                "intervalos nubosos con lluvia" -> IntervalosConLluvia
                "intervalos nubosos con lluvia escasa" -> IntervalosConLluvia
                "intervalos nubosos con tormenta y lluvia escasa" -> Tormenta
                "lluvia débil" -> LluviaDebil
                "lluvia moderada" -> LluviaModerada
                "lluvia fuerte" -> LluviaFuerte
                "chubascos" -> Chubascos
                "chubascos fuertes" -> ChubascosFuertes
                "nieve" -> Nieve
                "nieve débil" -> NieveDebil
                else -> Despejado
            }
        }
    }

}
