package id.allana.titipbarangku.ui.intro

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroCustomLayoutFragment.Companion.newInstance
import id.allana.titipbarangku.R
import id.allana.titipbarangku.ui.homepage.HomepageViewModel
import id.allana.titipbarangku.util.navigateToLoginScreen


class IntroScreenActivity : AppIntro2() {

    private val viewModel: HomepageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isSkipButtonEnabled = false

        setIndicatorColor(
            selectedIndicatorColor = ContextCompat.getColor(this, R.color.brown_700),
            unselectedIndicatorColor = ContextCompat.getColor(this, R.color.gray_item_divider)
        )

        addSlide(newInstance(R.layout.layout_intro_first))
        addSlide(newInstance(R.layout.layout_intro_second))
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        navigateToLoginScreen(this)
        setUserFirstTimeOpenApp()
    }

    private fun setUserFirstTimeOpenApp() = viewModel.setUserFirstTimeOpenApp(false)
}