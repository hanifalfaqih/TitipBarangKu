package id.allana.titipbarangku.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show()
}