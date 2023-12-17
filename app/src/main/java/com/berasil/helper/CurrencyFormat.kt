package com.berasil.helper

import java.text.NumberFormat
import java.util.Locale

class CurrencyFormat {

    fun rupiah(number: Int): String {
        val localeId = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeId)
        numberFormat.minimumFractionDigits = 0
        return numberFormat.format(number).toString()
    }
}