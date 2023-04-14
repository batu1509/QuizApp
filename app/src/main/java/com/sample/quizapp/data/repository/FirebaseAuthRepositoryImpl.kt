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
            var userUID = ""
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    userUID = it.user?.uid ?: ""
                }
                .await()
            dataStoreOperations.saveLoginInfo(userUID)
            userUID
        }catch (e:Exception){
            ""
        }
    }

    override suspend fun signUp(email: String, password: String): String {
        return try {
            var userUID = ""
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    userUID = it.user?.uid ?: ""
                }
                .await()
            userUID
        } catch (e:Exception){
            ""
        }
    }
}