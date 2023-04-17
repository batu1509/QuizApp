package com.sample.quizapp.domain.usecase

import android.content.ContentValues.TAG
import android.util.Log
import com.batueksi.moviesapp.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.sample.quizapp.data.models.User
import com.sample.quizapp.domain.repository.UserRepository
import com.sample.quizapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseSignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
){

//    suspend operator fun invoke(email: String, password: String, confirmPassword: String): Flow<Resource<Boolean>> = flow {
//        emit(Resource.Loading)
//        if (password != confirmPassword) {
//            emit(Resource.Error("Passwords do not match"))
//            return@flow
//        }
//        val userUID = authRepository.signUp(email, password, confirmPassword)
//        if (userUID.isNotEmpty()) {
//            userRepository.createUser(
//                User(
//                    email = email,
//                    uid = userUID
//                )
//            )
//            emit(Resource.Success(true))
//        } else {
//            emit(Resource.Error("Sign up error"))
//        }
//    }.catch { e ->
//        emit(Resource.Error("Sign up error: ${e.message}"))
//    }


    suspend operator fun invoke(email: String, password: String, confirmPassword: String, name: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        if (password != confirmPassword) {
            emit(Resource.Error("Passwords do not match"))
            return@flow
        }
        val userUID = authRepository.signUp(email, password, confirmPassword, name)
        if (userUID.isNotEmpty()) {
            val user = User(
                email = email,
                uid = userUID,
                name = name
            )
            userRepository.createUser(user)

            // Add user data to Firestore
            val userDocRef = FirebaseFirestore.getInstance().collection("users").document(userUID)
            userDocRef.set(user).await()

            emit(Resource.Success(true))
        } else {
            emit(Resource.Error("Sign up error"))
        }
    }.catch { e ->
        emit(Resource.Error("Sign up error: ${e.message}"))
    }




}