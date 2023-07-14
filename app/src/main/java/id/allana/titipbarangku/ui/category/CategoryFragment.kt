package id.allana.titipbarangku.ui.category

import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.databinding.FragmentCategoryBinding


class CategoryFragment : BaseFragment<FragmentCategoryBinding>(FragmentCategoryBinding::inflate) {

    override fun initView() {
        getViewBinding().fabAddCategory.setOnClickListener {
            CategoryBottomSheetFragment().show(childFragmentManager, TAG)
        }
    }

    companion object {
        private const val TAG = "CategoryBottomSheetFragment"
    }

}