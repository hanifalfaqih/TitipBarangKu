package id.allana.titipbarangku.util

import android.content.Context
import android.content.Intent
import id.allana.titipbarangku.MainActivity
import id.allana.titipbarangku.ui.intro.IntroScreenActivity
import id.allana.titipbarangku.ui.login.LoginActivity

fun navigateToIntroScreen(context: Context) {
    val intentToMain = Intent(context, IntroScreenActivity::class.java)
    intentToMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intentToMain)
}

fun navigateToLoginScreen(context: Context) {
    val intentToMain = Intent(context, LoginActivity::class.java)
    intentToMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intentToMain)
}

fun navigateToMainScreen(context: Context) {
    val intentToMain = Intent(context, MainActivity::class.java)
    intentToMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intentToMain)
}