package id.allana.titipbarangku.util

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

fun setLanguage(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val configuration = Configuration()
    configuration.setLocale(locale)

    // Update the configuration
    context.resources.updateConfiguration(configuration, context.resources.displayMetrics)

}