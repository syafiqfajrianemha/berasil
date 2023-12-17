package com.berasil.data.remote.response

import com.google.gson.annotations.SerializedName

data class CheckPriceResponse(

    @field:SerializedName("data")
    val data: List<DataItem>
)

data class DataItem(

    @field:SerializedName("SemuaPercentage")
    val semuaPercentage: Float,

    @field:SerializedName("Nilai")
    val nilai: Float,

    @field:SerializedName("SemuaProvinsi")
    val semuaProvinsi: Float,

    @field:SerializedName("Percentage")
    val percentage: Float,

    @field:SerializedName("show")
    val show: Boolean,

    @field:SerializedName("Komoditas")
    val komoditas: String,

    @field:SerializedName("TanggalInflasi")
    val tanggalInflasi: String,

    @field:SerializedName("stdDevPercentage")
    val stdDevPercentage: Any,

    @field:SerializedName("Kelompok")
    val kelompok: Int,

    @field:SerializedName("ProvID")
    val provID: Int,

    @field:SerializedName("Provinsi")
    val provinsi: String,

    @field:SerializedName("NilaiDiff")
    val nilaiDiff: String,

    @field:SerializedName("Tanggal")
    val tanggal: String,

    @field:SerializedName("stdDev")
    val stdDev: Float,

    @field:SerializedName("TanggalLast")
    val tanggalLast: String
)
