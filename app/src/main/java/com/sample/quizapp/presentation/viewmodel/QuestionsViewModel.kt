package com.sample.quizapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.quizapp.data.models.QuestionsModel
import com.sample.quizapp.data.repository.QuestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val questionRepository: QuestionsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val questions = MutableLiveData<List<QuestionsModel>>(emptyList())

    // list to keep track of selected answers
    val selectedAnswers = mutableListOf<String?>()

    init {
        val quizId = savedStateHandle.get<String>("quizId")
        quizId?.let {
            getQuestions(it)
        }
    }

    private fun getQuestions(quizId: String) {
        viewModelScope.launch {
            try {
                val questionsList = questionRepository.getQuestionsByQuizId(quizId)
                questions.postValue(questionsList)
                // initialize selectedAnswers list with null values
                selectedAnswers.addAll(List(questionsList.size) { null })
            } catch (e: Exception) {
            }
        }
    }


}



