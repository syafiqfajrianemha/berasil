package com.berasil.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.berasil.data.remote.local.UserPreferences
import com.berasil.data.remote.retrofit.cc.CcApiConfig
import com.berasil.data.remote.retrofit.ml.MlApiConfig
import com.berasil.data.repository.CcRepository
import com.berasil.data.repository.MlRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

object Injection {

    fun ccProvideRepository(context: Context): CcRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val ccApiService = CcApiConfig.getApiServiceCc()
        return CcRepository.getInstance(ccApiService, pref)
    }

    fun mlProvideRepository(): MlRepository {
        val mlApService = MlApiConfig.getApiServiceMl()
        return MlRepository.getInstance(mlApService)
    }
}