package id.allana.titipbarangku.util

import java.text.DecimalFormat
import java.util.Locale

fun formatRupiah(text: String): String {
    val cleanString = text.replace("[Rp,.\\s]".toRegex(), "")
    val convertToDecimal = cleanString.toDouble() / 100
    val formatter = DecimalFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(convertToDecimal)
}