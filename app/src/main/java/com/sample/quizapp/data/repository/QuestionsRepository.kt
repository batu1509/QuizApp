package com.sample.quizapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sample.quizapp.data.models.QuestionsModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuestionsRepository @Inject constructor(private val firestore: FirebaseFirestore) {

    suspend fun getQuestionsByQuizId(quizId: String): List<QuestionsModel> {
        val questions = mutableListOf<QuestionsModel>()
        val querySnapshot = firestore.collection("QuizList")
            .document(quizId)
            .collection("Questions")
            .get().await()
        for (doc in querySnapshot.documents) {
            val question = doc.toObject(QuestionsModel::class.java)
            question?.let { questions.add(it) }
        }
        return questions
    }
}
