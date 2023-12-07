package com.berasil.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.berasil.data.repository.CcRepository
import com.berasil.di.Injection
import com.berasil.ui.account.AccountViewModel
import com.berasil.ui.login.LoginViewModel
import com.berasil.ui.main.MainViewModel
import com.berasil.ui.register.RegisterViewModel

class CcViewModelFactory(private val ccRepository: CcRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(this.ccRepository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(this.ccRepository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(this.ccRepository) as T
        } else if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            return AccountViewModel(this.ccRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: CcViewModelFactory? = null

        fun getInstance(context: Context): CcViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: CcViewModelFactory(Injection.ccProvideRepository(context))
            }.also { instance = it }
    }
}