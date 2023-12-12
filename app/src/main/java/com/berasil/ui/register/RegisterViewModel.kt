package com.berasil.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berasil.data.remote.response.RegisterResponse
import com.berasil.data.repository.CcRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val repository: CcRepository) : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(name: String, email: String, password: String) {
        _isLoading.postValue(true)

        viewModelScope.launch {
            try {
                val response = repository.register(name, email, password)

                _isLoading.postValue(false)
                _registerResponse.postValue(response)
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
                val errorMessage = errorBody.message

                _isLoading.postValue(false)
                _registerResponse.postValue(errorBody)

                Log.d(TAG, "Register Error: $errorMessage")
            }
        }
    }

    companion object {
        private val TAG = RegisterViewModel::class.java.simpleName
    }
}