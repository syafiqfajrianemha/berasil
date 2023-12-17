package com.berasil.data.repository

import com.berasil.data.remote.retrofit.bi.BiApiService

class BiRepository private constructor(
    private val biApiService: BiApiService
) {

    fun getRicePrice(tanggal: String, commodity: String, priceType: Int, provId: Int) =
        biApiService.getRicePrice(tanggal, commodity, priceType, provId)

    companion object {
        @Volatile
        private var instance: BiRepository? = null

        fun getInstance(
            biApiService: BiApiService
        ): BiRepository =
            instance ?: synchronized(this) {
                instance ?: BiRepository(biApiService)
            }.also { instance = it }
    }
}