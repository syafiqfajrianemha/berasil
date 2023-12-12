package com.berasil.data.repository

import androidx.lifecycle.asLiveData
import com.berasil.data.remote.datastore.UserPreferences
import com.berasil.data.remote.datastore.model.UserModel
import com.berasil.data.remote.retrofit.cc.CcApiService

class CcRepository private constructor(
    private val ccApiService: CcApiService,
    private val userPreferences: UserPreferences,
) {

    suspend fun register(name: String, email: String, password: String) =
        ccApiService.register(name, email, password)

    suspend fun login(email: String, password: String) =
        ccApiService.login(email, password)

    fun getUser() = userPreferences.getUser().asLiveData()

    suspend fun saveUser(user: UserModel) = userPreferences.saveUser(user)

    suspend fun logout() = userPreferences.logout()

    companion object {
        @Volatile
        private var instance: CcRepository? = null

        fun getInstance(
            ccApiService: CcApiService,
            userPreferences: UserPreferences
        ): CcRepository =
            instance ?: synchronized(this) {
                instance ?: CcRepository(ccApiService, userPreferences)
            }.also { instance = it }
    }
}