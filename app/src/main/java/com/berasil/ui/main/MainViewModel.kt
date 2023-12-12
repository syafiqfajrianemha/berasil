package com.berasil.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.berasil.data.remote.datastore.model.UserModel
import com.berasil.data.repository.CcRepository

class MainViewModel(private val ccRepository: CcRepository) : ViewModel() {

    fun getUser(): LiveData<UserModel> = ccRepository.getUser()
}