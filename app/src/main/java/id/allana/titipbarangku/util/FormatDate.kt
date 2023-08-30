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

fun formatDateWithTime(dayOfMonth: Int, month: Int, year: Int, hour: Int, minute: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, dayOfMonth, hour, minute)

    val dateFormat = SimpleDateFormat("dd MMMM yyyy - HH:mm", Locale("id", "ID"))
    return dateFormat.format(calendar.time)
}