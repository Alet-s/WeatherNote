package com.alexser.weathernote.data.firebase

//class AuthDataSource @Inject constructor(
//    private val firebaseAuth: FirebaseAuth
//) {
//
//    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser
//
//    suspend fun signIn(email: String, password: String): Result<FirebaseUser> = withContext(Dispatchers.IO) {
//        try {
//            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
//            Result.success(result.user!!)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    fun signOut() {
//        firebaseAuth.signOut()
//    }
//}
