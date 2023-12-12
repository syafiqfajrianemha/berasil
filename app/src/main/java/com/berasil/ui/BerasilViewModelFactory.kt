package com.berasil.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.berasil.data.repository.BerasilRepository
import com.berasil.di.Injection
import com.berasil.ui.detection.result.ResultViewModel
import com.berasil.ui.history.HistoryViewModel

class BerasilViewModelFactory private constructor(private val repository: BerasilRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: BerasilViewModelFactory? = null

        fun getInstance(context: Context): BerasilViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: BerasilViewModelFactory(Injection.berasilProvideRepository(context))
            }.also { instance = it }
    }
}