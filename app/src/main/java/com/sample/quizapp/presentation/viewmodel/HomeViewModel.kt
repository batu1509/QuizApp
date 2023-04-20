package com.sample.quizapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.quizapp.data.models.QuizModel
import com.sample.quizapp.data.repository.QuizRepository
import com.sample.quizapp.domain.repository.DataStoreOperations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStoreOperations: DataStoreOperations,
    private val quizRepository: QuizRepository,
) : ViewModel() {

    val quizzes = MutableLiveData<List<QuizModel.UserModel>>()

    fun getQuizzes() {
        viewModelScope.launch {
            try {
                val quizList = quizRepository.getQuizzes()
                quizzes.postValue(quizList)
            } catch (e: Exception) {
            }
        }
    }

    fun addQuiz(quizModel: QuizModel.UserModel) {
        viewModelScope.launch {
            try {
                quizRepository.addQuiz(quizModel)
            } catch (e: Exception) {
            }
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            dataStoreOperations.deleteLoginInfo()
        }
    }

}