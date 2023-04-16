package com.sample.quizapp.data.repository

import com.sample.quizapp.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sample.quizapp.Constants.USERS_COLLECTION
import com.sample.quizapp.data.models.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserRepositoryImpl @Inject constructor(): UserRepository {

    override suspend fun createUser(user: User): Boolean {
        return try {
            val result = FirebaseFirestore.getInstance().collection(USERS_COLLECTION)
                .document(user.uid)
                .set(user, SetOptions.merge())
                .await()
            result != null
        } catch (e:Exception){
            false
        }
    }

    override suspend fun getUser(uid: String): User {
        return try {
            val document = FirebaseFirestore.getInstance().collection(USERS_COLLECTION)
                .document(uid)
                .get()
                .await()
            document.toObject(User::class.java) ?: User()
        } catch (e: Exception) {
            User()
        }
    }

}