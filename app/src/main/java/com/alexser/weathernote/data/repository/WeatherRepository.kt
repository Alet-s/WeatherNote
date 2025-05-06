package com.alexser.weathernote.data.repository

//class WeatherRepositoryImpl @Inject constructor(
//    private val aemetService: AemetService,
//    private val firestoreDataSource: FirestoreDataSource
//) : WeatherRepository {
//
//    override suspend fun getDailyWeatherReport(municipioId: String): WeatherReport? {
//        val dto = aemetService.getForecast(municipioId)?.firstOrNull()
//        return dto?.toDomain()
//    }
//
//    override suspend fun saveReportToFirebase(report: WeatherReport) {
//        firestoreDataSource.saveReport(report)
//    }
//
//    override suspend fun getUserReports(): List<WeatherReport> {
//        return firestoreDataSource.getReports()
//    }
//}
