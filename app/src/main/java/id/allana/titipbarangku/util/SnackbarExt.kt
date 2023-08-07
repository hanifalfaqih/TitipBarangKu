package id.allana.titipbarangku.util

import android.view.View
import com.google.android.material.snackbar.Snackbar
import id.allana.titipbarangku.util.ConstantValue.SNACKBAR_DURATION

fun View.snackbar(msg: String, viewId: Int) {
    val snackbar = Snackbar.make(this, msg, SNACKBAR_DURATION)
    snackbar.setAnchorView(viewId)
    snackbar.show()
}