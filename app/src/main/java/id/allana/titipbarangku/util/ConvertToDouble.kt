package id.allana.titipbarangku.util

fun convertToDouble(inputString: String): Double? {
    // Menghilangkan simbol mata uang dan tanda titik sebagai pemisah ribuan
    val cleanedString = inputString.replace("Rp", "").replace(".", "").replace(",", ".")

    // Mengonversi string menjadi double
    return try {
        cleanedString.toDouble()
    } catch (e: NumberFormatException) {
        null // Jika gagal mengonversi, kembalikan nilai null
    }
}