package com.berasil.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.berasil.data.local.entity.ResultDetection

@Dao
interface BerasilDao {

    @Query("SELECT * FROM results ORDER BY id DESC")
    fun getAllResult(): LiveData<List<ResultDetection>>

    @Insert
    fun insertResult(resultDetection: ResultDetection)

    @Delete
    fun deleteResult(resultDetection: ResultDetection)
}