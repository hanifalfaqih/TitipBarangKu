package id.allana.titipbarangku.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.viewModels
import id.allana.titipbarangku.MainActivity
import id.allana.titipbarangku.R
import id.allana.titipbarangku.ui.homepage.HomepageViewModel
import id.allana.titipbarangku.ui.intro.IntroScreenActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val viewModel: HomepageViewModel by viewModels()

    private val timer: CountDownTimer by lazy {
        object: CountDownTimer(3000, 1000) {
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                viewModel.getUserFirstTimeOpenApp().observe(this@SplashScreenActivity) { isFirstTimeOpen ->
                    if (isFirstTimeOpen) {
                        navigateToIntroScreen()
                    } else {
                        navigateToHomepageScreen()
                    }
                }
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        timer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    private fun navigateToIntroScreen() {
        val intentToMain = Intent(this@SplashScreenActivity, IntroScreenActivity::class.java)
        intentToMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intentToMain)
    }
    private fun navigateToHomepageScreen() {
        val intentToMain = Intent(this@SplashScreenActivity, MainActivity::class.java)
        intentToMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intentToMain)
    }
}