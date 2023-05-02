package com.sample.quizapp.data.models

import com.google.firebase.firestore.DocumentId


    data class QuestionsModel(
        @DocumentId
        val questionId: String = "",
        val questionNumber: Int = 0,
        val question: String = "",
        val option_a: String = "",
        val option_b: String = "",
        val option_c: String = "",
        val answer: String = "",
        val timer: Long = 0
    )
