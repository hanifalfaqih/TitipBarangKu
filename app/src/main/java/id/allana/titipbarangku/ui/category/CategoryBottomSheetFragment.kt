package id.allana.titipbarangku.ui.category

import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import id.allana.titipbarangku.data.base.BaseBottomSheetDialogFragment
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.databinding.FragmentCategoryBottomSheetBinding


class CategoryBottomSheetFragment : BaseBottomSheetDialogFragment<FragmentCategoryBottomSheetBinding>(FragmentCategoryBottomSheetBinding::inflate) {

    private val viewModel: CategoryViewModel by viewModels()

    override fun initView() {
        getViewBinding().btnAddCategory.setOnClickListener {
            insertCategory()
        }
    }

    private fun insertCategory() {
        if (validateForm()) {
            val category = CategoryModel(0, categoryName = getViewBinding().etCategory.text.toString())
            viewModel.insertCategory(category)
            Snackbar.make(requireActivity().findViewById(android.R.id.content), "Berhasil tambah kategori", Snackbar.LENGTH_SHORT).show().run {
                this@CategoryBottomSheetFragment.dismiss()
            }
        }
    }

    private fun validateForm(): Boolean {
        val textCategory = getViewBinding().etCategory.text.toString()
        val isFormValid: Boolean

        if (textCategory.isEmpty()) {
            isFormValid = false
            getViewBinding().etCategory.error = "Kategori harus diisi!"
        } else isFormValid = true
        return isFormValid
    }
}