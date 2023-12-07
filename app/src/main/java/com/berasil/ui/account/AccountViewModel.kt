package com.berasil.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berasil.data.remote.local.model.UserModel
import com.berasil.data.repository.CcRepository
import kotlinx.coroutines.launch

class AccountViewModel(private val ccRepository: CcRepository) : ViewModel() {

    fun getUser(): LiveData<UserModel> = ccRepository.getUser()

    fun logout() {
        viewModelScope.launch {
            ccRepository.logout()
        }
    }
}