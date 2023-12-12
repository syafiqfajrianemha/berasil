package com.berasil.ui.history

import androidx.lifecycle.ViewModel
import com.berasil.data.local.entity.ResultDetection
import com.berasil.data.repository.BerasilRepository

class HistoryViewModel(private val repository: BerasilRepository) : ViewModel() {

    fun getAllResult() = repository.getAllResult()

    fun deleteResult(resultDetection: ResultDetection) {
        repository.deleteResult(resultDetection)
    }
}