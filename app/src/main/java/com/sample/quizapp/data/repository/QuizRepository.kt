package com.sample.quizapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sample.quizapp.data.models.QuizModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val quizCollection = firestore.collection(QuizModel.COLLECTION_NAME)

    suspend fun getQuizzes(): List<QuizModel.UserModel> {
        return quizCollection
            .orderBy(QuizModel.FIELD_NAME)
            .get()
            .await()
            .toObjects(QuizModel.UserModel::class.java)
    }

    suspend fun addQuiz(quizModel: QuizModel.UserModel) {
        quizCollection.document(quizModel.quiz_id).set(quizModel).await()
    }

    suspend fun getQuizById(quizId: String): QuizModel.UserModel? {
        return quizCollection.document(quizId)
            .get()
            .await()
            .toObject(QuizModel.UserModel::class.java)
    }
}



