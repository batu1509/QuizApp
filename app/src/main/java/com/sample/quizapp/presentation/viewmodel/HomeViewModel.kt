package com.sample.quizapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.quizapp.domain.repository.DataStoreOperations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataStoreOperations: DataStoreOperations
) : ViewModel() {



    fun logoutUser() {
        viewModelScope.launch {
            dataStoreOperations.deleteLoginInfo()
        }
    }

}