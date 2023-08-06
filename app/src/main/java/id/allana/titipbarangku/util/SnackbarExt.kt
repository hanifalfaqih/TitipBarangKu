package id.allana.titipbarangku.util

import android.view.View
import com.google.android.material.snackbar.Snackbar
import id.allana.titipbarangku.R

fun View.snackbar(msg: String, view: View) {
    val snackbar = Snackbar.make(this, msg, Snackbar.LENGTH_SHORT)
    snackbar.setAction(context.getString(R.string.close)) {
        snackbar.dismiss()
    }
    snackbar.setAnchorView(view)
    snackbar.show()
}