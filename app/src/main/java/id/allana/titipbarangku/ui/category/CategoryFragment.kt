package id.allana.titipbarangku.ui.category

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.data.model.CategoryModel
import id.allana.titipbarangku.databinding.FragmentCategoryBinding
import id.allana.titipbarangku.ui.category.adapter.CategoryAdapter
import id.allana.titipbarangku.util.snackbar

class CategoryFragment : BaseFragment<FragmentCategoryBinding>(FragmentCategoryBinding::inflate) {

    private val viewModel: CategoryViewModel by viewModels()
    private val categoryAdapter by lazy {
        CategoryAdapter { deleteItem ->
            showAlertDialog(deleteItem)
        }
    }

    override fun initView() {
        initRecyclerView()
        getViewBinding().fabAddCategory.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_category_to_categoryBottomSheetFragment)
        }
    }

    private fun initRecyclerView() {
        getViewBinding().rvCategory.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = this@CategoryFragment.categoryAdapter
        }
    }

    private fun showAlertDialog(data: CategoryModel) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.delete_data))
            setMessage(getString(R.string.msg_delete_data, data.categoryName))
            setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteCategory(data)
                requireView().snackbar(getString(R.string.success_delete_data, data.categoryName))
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    override fun observeData() {
        viewModel.getAllCategory().observe(viewLifecycleOwner) { listCategory ->
            viewModel.checkDatabaseEmpty(listCategory)
            categoryAdapter.submitList(listCategory)
        }
        viewModel.checkDatabaseEmptyLiveData().observe(viewLifecycleOwner) { isEmpty ->
            if (isEmpty) {
                stateDataEmpty(true)
            } else {
                stateDataEmpty(false)
            }
        }
    }

    private fun stateDataEmpty(isEmpty: Boolean) {
        getViewBinding().also {
            if (isEmpty) {
                it.ivStateDataEmpty.visibility = View.VISIBLE
                it.tvStateDataEmpty.visibility = View.VISIBLE
            } else {
                it.ivStateDataEmpty.visibility = View.GONE
                it.tvStateDataEmpty.visibility = View.GONE
            }
        }
    }
}