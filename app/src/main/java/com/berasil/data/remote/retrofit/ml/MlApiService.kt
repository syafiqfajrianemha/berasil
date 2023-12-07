package com.berasil.data.remote.retrofit.ml

import com.berasil.data.remote.response.DetectionResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MlApiService {

    @Multipart
    @POST("detectImage")
    suspend fun detectionImage(
        @Part file: MultipartBody.Part
    ): DetectionResponse
}