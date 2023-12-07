package com.berasil.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetectionResponse(

    @field:SerializedName("batu")
    val batu: Int,

    @field:SerializedName("butir_gabah")
    val butirGabah: Int,

    @field:SerializedName("butir_kapur")
    val butirKapur: Int,

    @field:SerializedName("butir_kepala")
    val butirKepala: Int,

    @field:SerializedName("butir_menir")
    val butirMenir: Int,

    @field:SerializedName("butir_merah")
    val butirMerah: Int,

    @field:SerializedName("butir_patah")
    val butirPatah: Int,

    @field:SerializedName("butir_rusak")
    val butirRusak: Int,

    @field:SerializedName("kutu")
    val kutu: Int,

    @field:SerializedName("sekam")
    val sekam: Int,

    @field:SerializedName("url")
    val url: String
)
