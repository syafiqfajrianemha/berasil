package com.berasil.data.repository

import androidx.lifecycle.LiveData
import com.berasil.data.local.BerasilDao
import com.berasil.data.local.entity.ResultDetection
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BerasilRepository private constructor(
    private val berasilDao: BerasilDao,
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
) {

    fun getAllResult(): LiveData<List<ResultDetection>> = berasilDao.getAllResult()

    fun insertResult(result: ResultDetection) {
        executorService.execute { berasilDao.insertResult(result) }
    }

    fun deleteResult(result: ResultDetection) {
        executorService.execute { berasilDao.deleteResult(result) }
    }

    companion object {
        @Volatile
        private var instance: BerasilRepository? = null

        fun getInstance(
            berasilDao: BerasilDao
        ): BerasilRepository =
            instance ?: synchronized(this) {
                instance ?: BerasilRepository(berasilDao)
            }.also { instance = it }
    }
}