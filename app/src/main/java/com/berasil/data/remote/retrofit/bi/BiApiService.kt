package com.berasil.data.remote.retrofit.bi

import com.berasil.data.remote.response.CheckPriceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BiApiService {

    @GET("Home/GetGridData1")
    fun getRicePrice(
        @Query("tanggal") tanggal: String,
        @Query("commodity") commodity: String,
        @Query("priceType") priceType: Int,
        @Query("provId") provId: Int
    ): Call<CheckPriceResponse>
}