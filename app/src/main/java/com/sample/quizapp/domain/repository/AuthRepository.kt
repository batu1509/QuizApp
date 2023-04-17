package com.batueksi.moviesapp.domain.repository

interface AuthRepository {

    suspend fun login(email:String , password: String): String

    suspend fun signUp(email: String, password: String, confirmPassword: String, name: String): String
}