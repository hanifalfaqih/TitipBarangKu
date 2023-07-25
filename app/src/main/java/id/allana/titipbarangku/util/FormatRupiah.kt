package id.allana.titipbarangku.util

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(text: String): String {
    val convertToDouble = text.toDouble()
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(convertToDouble)
}