package id.allana.titipbarangku.ui.category

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseBottomSheetDialogFragment
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.databinding.FragmentCategoryBottomSheetBinding
import id.allana.titipbarangku.util.snackbar


class CategoryBottomSheetFragment : BaseBottomSheetDialogFragment<FragmentCategoryBottomSheetBinding>(FragmentCategoryBottomSheetBinding::inflate) {

    private val viewModel: CategoryViewModel by viewModels()
    private val args by navArgs<CategoryBottomSheetFragmentArgs>()

    override fun initView() {
        getViewBinding().btnAddCategory.setOnClickListener {
            insertCategory(0)
        }
        args.categoryData?.let { categoryModel ->
            /**
             * set category name from args to edit text
             */
            getViewBinding().etCategory.setText(categoryModel.categoryName)
            /**
             * change button text from add to update
             */
            getViewBinding().btnAddCategory.text = getString(R.string.update)
            /**
             * when button clicked if args not null, it will update data
             */
            getViewBinding().btnAddCategory.setOnClickListener {
                insertCategory(categoryModel.id)
            }
        }
    }

    private fun insertCategory(id: Int) {
        if (validateForm()) {
            val category = CategoryModel(0, categoryName = getViewBinding().etCategory.text.toString())

            val successMessage = if (id == 0) {
                viewModel.insertCategory(category)
                getString(R.string.success_add_category)
            } else {
                viewModel.updateCategory(category)
                getString(R.string.success_update_category)
            }

            requireActivity().findViewById<View>(android.R.id.content).snackbar(successMessage).also { this.dismiss() }
        }
    }

    private fun validateForm(): Boolean {
        val textCategory = getViewBinding().etCategory.text.toString()
        val isFormValid: Boolean

        if (textCategory.isEmpty()) {
            isFormValid = false
            getViewBinding().etCategory.error = getString(R.string.category_must_be_filled)
        } else isFormValid = true
        return isFormValid
    }
}