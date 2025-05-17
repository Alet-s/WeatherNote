package com.alexser.weathernote.domain.model

import com.alexser.weathernote.R

sealed class CondicionMeteorologica(val codigo: String, val descripcion: String, val iconoRes: Int) {
    object Despejado : CondicionMeteorologica("11", "Despejado", R.drawable.wi_moon_new)
    object PocoNuboso : CondicionMeteorologica("12", "Poco nuboso", R.drawable.wi_night_partly_cloudy)
    object Nuboso : CondicionMeteorologica("13", "Nuboso", R.drawable.wi_night_cloudy)
    object MuyNuboso : CondicionMeteorologica("14", "Muy nuboso", R.drawable.wi_cloudy)
    object Cubierto : CondicionMeteorologica("15", "Cubierto", R.drawable.wi_cloud)
    object NubesAltas : CondicionMeteorologica("16", "Nubes altas", R.drawable.wi_night_partly_cloudy)
    object IntervalosConLluvia : CondicionMeteorologica("17", "Intervalos nubosos con lluvia", R.drawable.wi_night_rain_mix)
    object NubosoConLluvia : CondicionMeteorologica("18", "Nuboso con lluvia", R.drawable.wi_night_rain_mix)
    object MuyNubosoConLluvia : CondicionMeteorologica("19", "Muy nuboso con lluvia", R.drawable.wi_rain)
    object CubiertoConLluvia : CondicionMeteorologica("20", "Cubierto con lluvia", R.drawable.wi_rain)
    object Tormenta : CondicionMeteorologica("21", "Cubierto con tormenta", R.drawable.wi_night_thunderstorm)
    object Nieblas : CondicionMeteorologica("22", "Nieblas", R.drawable.wi_windy)
    object NieblaDensa : CondicionMeteorologica("23", "Niebla densa", R.drawable.wi)
    object LluviaDebil : CondicionMeteorologica("24", "Lluvia débil", R.drawable.wi_raindrops)
    object LluviaModerada : CondicionMeteorologica("25", "Lluvia moderada", R.drawable.wi_night_rain_mix)
    object LluviaFuerte : CondicionMeteorologica("26", "Lluvia fuerte", R.drawable.wi_rain)
    object Chubascos : CondicionMeteorologica("27", "Chubascos", R.drawable.wi_night_rain_mix)
    object ChubascosFuertes : CondicionMeteorologica("28", "Chubascos fuertes", R.drawable.wi_night_rain)
    object Nieve : CondicionMeteorologica("29", "Nieve", R.drawable.wi_snow)
    object NieveDebil : CondicionMeteorologica("30", "Nieve débil", R.drawable.wi_night_snow_wind)
    object Granizo : CondicionMeteorologica("31", "Granizo", R.drawable.wi_hail)
    object TormentaElectrica : CondicionMeteorologica("32", "Tormenta", R.drawable.wi_night_thunderstorm)

    companion object {
        fun desdeCodigo(codigo: String): CondicionMeteorologica = when (codigo) {
            "11" -> Despejado
            "12" -> PocoNuboso
            "13" -> Nuboso
            "14" -> MuyNuboso
            "15" -> Cubierto
            "16" -> NubesAltas
            "17" -> IntervalosConLluvia
            "18" -> NubosoConLluvia
            "19" -> MuyNubosoConLluvia
            "20" -> CubiertoConLluvia
            "21" -> Tormenta
            "22" -> Nieblas
            "23" -> NieblaDensa
            "24" -> LluviaDebil
            "25" -> LluviaModerada
            "26" -> LluviaFuerte
            "27" -> Chubascos
            "28" -> ChubascosFuertes
            "29" -> Nieve
            "30" -> NieveDebil
            "31" -> Granizo
            "32" -> TormentaElectrica
            else -> Despejado // Fallback
        }
    }
}
