package com.sample.quizapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.quizapp.data.models.QuizModel
import com.sample.quizapp.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizDetailViewModel@Inject constructor(
    private val quizRepository: QuizRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val quiz = MutableLiveData<QuizModel.UserModel>()

    init {
        val quizId = savedStateHandle.get<String>("quizId")
        quizId?.let {
            getQuizById(it)
        }
    }

    fun getQuizById(quizId: String?) {
        quizId?.let {
            viewModelScope.launch {
                try {
                    val quizModel = quizRepository.getQuizById(quizId)
                    quiz.postValue(quizModel!!)
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
}