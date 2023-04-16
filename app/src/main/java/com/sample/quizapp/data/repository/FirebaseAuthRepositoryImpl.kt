package com.sample.quizapp.data.repository

import com.batueksi.moviesapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
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

    override suspend fun signUp(email: String, password: String, confirmPassword: String): String {
        if (password != confirmPassword) {
            throw Exception("Passwords do not match")
        }
        return try {
            val userCredential = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            userCredential.user?.uid ?: ""
        } catch (e: Exception) {
            ""
        }
    }



}