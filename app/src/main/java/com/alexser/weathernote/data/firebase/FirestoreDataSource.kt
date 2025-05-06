package com.alexser.weathernote.data.firebase

//class FirestoreDataSource @Inject constructor(
//    private val firestore: FirebaseFirestore,
//    private val auth: FirebaseAuth
//) {
//
//    private fun userReportsCollection(): CollectionReference {
//        val uid = auth.currentUser?.uid ?: throw Exception("User not authenticated")
//        return firestore.collection("users").document(uid).collection("weather_reports")
//    }
//
//    suspend fun saveReport(report: WeatherReport) {
//        val reportData = report.toFirebaseMap()
//        userReportsCollection().add(reportData).await()
//    }
//
//    suspend fun getReports(): List<WeatherReport> {
//        val snapshot = userReportsCollection().get().await()
//        return snapshot.documents.mapNotNull { it.toWeatherReport() }
//    }
//}
