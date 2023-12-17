package com.berasil.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.berasil.data.repository.BiRepository
import com.berasil.di.Injection
import com.berasil.ui.price.DialogCheckPriceViewModel

class BiViewModelFactory(private val biRepository: BiRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DialogCheckPriceViewModel::class.java)) {
            return DialogCheckPriceViewModel(this.biRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: BiViewModelFactory? = null

        fun getInstance(): BiViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: BiViewModelFactory(Injection.biProvideRepository())
            }.also { instance = it }
    }
}