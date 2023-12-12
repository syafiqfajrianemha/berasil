package com.berasil.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "results")
@Parcelize
data class ResultDetection(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "quality")
    var quality: String? = null,

    @ColumnInfo(name = "createdAt")
    var createdAt: String? = null,

    @ColumnInfo(name = "butirKepala")
    val butirKepala: String? = null,

    @ColumnInfo(name = "butirPatah")
    val butirPatah: String? = null,

    @ColumnInfo(name = "butirMenir")
    val butirMenir: String? = null,

    @ColumnInfo(name = "butirMerah")
    val butirMerah: String? = null,

    @ColumnInfo(name = "butirRusak")
    val butirRusak: String? = null,

    @ColumnInfo(name = "butirKapur")
    val butirKapur: String? = null,

    @ColumnInfo(name = "butirGabah")
    val butirGabah: String? = null,

    @ColumnInfo(name = "sekam")
    val sekam: String? = null,

    @ColumnInfo(name = "kutu")
    val kutu: String? = null,

    @ColumnInfo(name = "batu")
    val batu: String? = null,

    @ColumnInfo(name = "total")
    val total: String? = null,

    @ColumnInfo(name = "url")
    val url: String? = null
) : Parcelable