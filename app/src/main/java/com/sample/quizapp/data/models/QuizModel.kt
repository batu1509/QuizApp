package com.sample.quizapp.data.models

import com.google.firebase.firestore.DocumentId

class QuizModel {
    data class UserModel(
        @DocumentId
        val quiz_id: String = "",
        val name: String = "",
        val level: String = "",
        val desc: String = "",
        val questions: Long = 0,
        val visibility: String = "",
        val image: String = ""
    )

// Firestore belge alanları
companion object {
    const val COLLECTION_NAME = "QuizList"
    const val FIELD_QUIZ_ID = "quiz_id"
    const val FIELD_NAME = "name"
    const val FIELD_LEVEL = "level"
    const val FIELD_DESC = "desc"
    const val FIELD_QUESTIONS = "questions"
    const val FIELD_VISIBILITY = "visibility"
    const val FIELD_IMAGE = "image"
}
}
