package com.sample.quizapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.sample.quizapp.Constants.LOGIN_KEY
import com.sample.quizapp.domain.repository.DataStoreOperations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DataStoreOperationsImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreOperations {

    override suspend fun saveLoginInfo(uid: String) {
        val key = stringPreferencesKey(LOGIN_KEY)
        dataStore.edit {
            it[key] = uid
        }
    }

    override fun getLoginInfo(): Flow<String?> {
        val key = stringPreferencesKey(LOGIN_KEY)
        return dataStore.data.map {
            it[key]
        }
    }

    override suspend fun deleteLoginInfo() {
        val key = stringPreferencesKey(LOGIN_KEY)
        dataStore.edit {
            it[key] = ""
        }
    }
}
