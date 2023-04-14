package com.sample.quizapp.data.repository

import com.sample.quizapp.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sample.quizapp.data.models.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserRepositoryImpl @Inject constructor(): UserRepository {

    companion object {
        const val USERS_COLLECTION = "users"
    }

    override suspend fun createUser(user: User): Boolean {
        return try {
            var isSuccessfull = false
            FirebaseFirestore.getInstance().collection(USERS_COLLECTION)
                .document(user.uid)
                .set(user, SetOptions.merge())
                .addOnCompleteListener { isSuccessfull = it.isSuccessful }
                .await()
            isSuccessfull
        } catch (e:Exception){
            false
        }
    }

    override suspend fun getUser(uid: String): User {
        return try {
            var loggedUser = User()
            FirebaseFirestore.getInstance().collection(USERS_COLLECTION)
                .document(uid)
                .get()
                .addOnSuccessListener {
                    loggedUser = it.toObject(User::class.java)!!
                }
                .await()
            loggedUser
        } catch (e:Exception){
            User()
        }
    }
}