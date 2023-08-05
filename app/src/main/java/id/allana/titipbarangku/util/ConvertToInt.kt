package id.allana.titipbarangku.util

fun convertToInt(inputString: String): Int {
    // Menghilangkan simbol mata uang dan tanda titik sebagai pemisah ribuan
    val cleanedString = inputString.replace("[^\\d]".toRegex(), "")

    // Mengonversi string menjadi double
    return cleanedString.toInt()
}