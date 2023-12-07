package com.berasil.ui.detection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berasil.data.remote.response.DetectionResponse
import com.berasil.data.repository.MlRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class DetectionViewModel(private val mlRepository: MlRepository) : ViewModel() {

    private val _detectionResponse = MutableLiveData<DetectionResponse>()
    val detectionResponse: LiveData<DetectionResponse> = _detectionResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun detectionImage(file: MultipartBody.Part) {
        _isLoading.value = true

        viewModelScope.launch {
            val response = mlRepository.detectionImage(file)

            _detectionResponse.postValue(response)
            _isLoading.value = false
        }
    }
}