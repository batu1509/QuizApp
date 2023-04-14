package com.sample.quizapp.domain.repository

import com.sample.quizapp.data.models.User

interface UserRepository {

    suspend fun createUser(user: User) : Boolean

    suspend fun getUser(uid: String): User
}