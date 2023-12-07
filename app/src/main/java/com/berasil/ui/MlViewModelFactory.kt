package com.berasil.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.berasil.data.repository.MlRepository
import com.berasil.di.Injection
import com.berasil.ui.detection.DetectionViewModel

class MlViewModelFactory(private val mlRepository: MlRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetectionViewModel::class.java)) {
            return DetectionViewModel(this.mlRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: MlViewModelFactory? = null

        fun getInstance(): MlViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MlViewModelFactory(Injection.mlProvideRepository())
            }.also { instance = it }
    }
}