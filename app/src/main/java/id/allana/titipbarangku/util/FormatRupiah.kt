package id.allana.titipbarangku.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun formatRupiah(text: String): String {
    val cleanString = text.replace("[Rp,.\\s]".toRegex(), "")
    val convertToLong = cleanString.toLong()
    val formatter = DecimalFormat("Rp#,###", DecimalFormatSymbols(Locale("id", "ID")))//DecimalFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(convertToLong)
}