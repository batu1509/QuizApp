package com.sample.quizapp.presentation.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.sample.quizapp.domain.repository.UserRepository
import javax.inject.Inject

class AuthenticationViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val dataStore: DataStore<Preferences>,
    private val userRepository: UserRepository
) : ViewModel() {
}