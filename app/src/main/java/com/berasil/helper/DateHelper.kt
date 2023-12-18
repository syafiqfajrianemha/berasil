package com.berasil.helper

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateHelper {

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy • HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun getYesterdayDate(): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.DAY_OF_MONTH, -1)
        val yesterdayDate = currentDate.time
        return dateFormat.format(yesterdayDate)
    }
}