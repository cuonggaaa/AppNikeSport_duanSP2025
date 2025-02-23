package com.example.smeb9716.utils.ext

import android.os.Build
import android.widget.TextView
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun String.toMoneyFormat(): String {
    val number = this.toLongOrNull() ?: 0
    return String.format(Locale.US, "%,d", number)
}

fun TextView.currentDate() {
    try {
        val date = Date()
        val format = SimpleDateFormat("dd/MM/yyy", Locale.getDefault())
        this.text = format.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun String.toDMYFormat(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(this)
        outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        this
    }
}