package com.sample.quizapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreOperations {

    suspend fun saveLoginInfo(uid: String)

    fun getLoginInfo(): Flow<String?>

    suspend fun  deleteLoginInfo()
}