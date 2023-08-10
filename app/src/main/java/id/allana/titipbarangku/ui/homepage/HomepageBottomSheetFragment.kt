package id.allana.titipbarangku.ui.homepage

import androidx.fragment.app.viewModels
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseBottomSheetDialogFragment
import id.allana.titipbarangku.databinding.FragmentHomepageBottomSheetBinding


class HomepageBottomSheetFragment : BaseBottomSheetDialogFragment<FragmentHomepageBottomSheetBinding>(FragmentHomepageBottomSheetBinding::inflate) {

    private val viewModel: HomepageViewModel by viewModels()

    override fun initView() {
        isCancelable = false
        
        getViewBinding().btnSaveStoreName.setOnClickListener {
            insertStoreName()
        }
    }

    private fun insertStoreName() {
        if (validateForm()) {
            val storeName = getViewBinding().etStoreName.text.toString()
            viewModel.setUserStoreName(storeName)
            this.dismiss()
        }
    }

    private fun validateForm(): Boolean {
        val textStoreName = getViewBinding().etStoreName.text.toString().trim()
        val isFormValid: Boolean

        when {
            textStoreName.isEmpty() -> {
                isFormValid = false
                getViewBinding().etStoreName.error = getString(R.string.store_name_must_be_filled)
            }

            else -> {
                isFormValid = true
            }
        }
        return isFormValid
    }

}