package com.berasil.ui.detection.result

import androidx.lifecycle.ViewModel
import com.berasil.data.local.entity.ResultDetection
import com.berasil.data.repository.BerasilRepository

class ResultViewModel(private val repository: BerasilRepository) : ViewModel() {

    fun insertResult(resultDetection: ResultDetection) {
        repository.insertResult(resultDetection)
    }
}