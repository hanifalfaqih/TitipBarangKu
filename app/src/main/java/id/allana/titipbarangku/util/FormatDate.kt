package id.allana.titipbarangku.util

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, dayOfMonth)

    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
    return dateFormat.format(calendar.time)

}