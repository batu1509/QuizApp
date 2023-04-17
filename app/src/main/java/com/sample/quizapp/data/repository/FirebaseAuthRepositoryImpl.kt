package com.sample.quizapp.data.repository

import com.batueksi.moviesapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.sample.quizapp.Constants.USERS_COLLECTION
import com.sample.quizapp.domain.repository.DataStoreOperations
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth : FirebaseAuth,
    private val dataStoreOperations: DataStoreOperations
): AuthRepository {

    override suspend fun login(email: String, password: String): String {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .await()
                .user
                ?.uid
                ?.let { userUID ->
                    dataStoreOperations.saveLoginInfo(userUID)
                    userUID
                } ?: ""
        }catch (e:Exception){
            ""
        }
    }

//    override suspend fun signUp(email: String, password: String, confirmPassword: String): String {
//        if (password != confirmPassword) {
//            throw Exception("Passwords do not match")
//        }
//        return try {
//            val userCredential = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
//            userCredential.user?.uid ?: ""
//        } catch (e: Exception) {
//            ""
//        }
//    }

    override suspend fun signUp(email: String, password: String, confirmPassword: String, name: String): String {
        if (password != confirmPassword) {
            throw Exception("Passwords do not match")
        }
        return try {
            val userCredential = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = userCredential.user
            user?.let {
                // Set user display name
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                it.updateProfile(profileUpdates).await()

                // Create user document in Firestore
                val userDoc = FirebaseFirestore.getInstance().collection(USERS_COLLECTION).document(it.uid)
                val userData = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "uid" to it.uid
                )
                userDoc.set(userData).await()

                return it.uid
            } ?: throw Exception("Sign up error")
        } catch (e: Exception) {
            throw Exception("Sign up error: ${e.message}")
        }
    }




}