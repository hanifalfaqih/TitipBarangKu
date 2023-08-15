package id.allana.titipbarangku.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.allana.titipbarangku.R
import id.allana.titipbarangku.ui.homepage.HomepageViewModel
import id.allana.titipbarangku.util.navigateToIntroScreen
import id.allana.titipbarangku.util.navigateToLoginScreen
import id.allana.titipbarangku.util.navigateToMainScreen

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val viewModel: HomepageViewModel by viewModels()

    private val timer: CountDownTimer by lazy {
        object: CountDownTimer(3000, 1000) {
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                viewModel.getUserFirstTimeOpenApp().observe(this@SplashScreenActivity) { isFirstTimeOpen ->
                    viewModel.getAuthUserLiveData().observe(this@SplashScreenActivity) { authUser ->
                        Log.d(SplashScreenActivity::class.java.simpleName, "DATA AUTH USER: $authUser")
                        if (isFirstTimeOpen) {
                            navigateToIntroScreen(this@SplashScreenActivity)
                        } else if (authUser == "") {
                            navigateToLoginScreen(this@SplashScreenActivity)
                        } else {
                            navigateToMainScreen(this@SplashScreenActivity)
                        }
                    }
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        viewModel.getAuthUser(this)
        timer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}