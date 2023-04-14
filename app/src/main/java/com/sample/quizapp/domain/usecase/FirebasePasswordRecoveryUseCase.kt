package com.sample.quizapp.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.sample.quizapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebasePasswordRecoveryUseCase @Inject constructor() {

    suspend operator fun invoke(email: String): Flow<Resource<Boolean>> = flow{
         try {
            emit(Resource.Loading)
            var isSuccessFul = false
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { isSuccessFul = it.isSuccessful }
                .await()
            emit(Resource.Success(isSuccessFul))
            emit(Resource.Finished)
        } catch (e : Exception) {
            emit(Resource.Error(e.message.toString()))
            emit(Resource.Finished)
        }

    }
}