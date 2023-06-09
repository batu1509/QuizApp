package com.sample.quizapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.quizapp.domain.usecase.FirebaseSignUpUseCase
import com.sample.quizapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val signUpUseCase: FirebaseSignUpUseCase): ViewModel() {


    private val _signUpState: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val signUpState: LiveData<Resource<Boolean>>
        get() = _signUpState

    fun signUp(email:String, password:String, confirmPassword: String, name: String) {
        if (password != confirmPassword) {
            // Confirm password doesn't match, handle the error
            return
        }
        viewModelScope.launch {
            signUpUseCase(email, password, confirmPassword, name).onEach { state ->
                _signUpState.value = state
            }.launchIn(viewModelScope)
        }
    }

}