package id.allana.titipbarangku.ui.product

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.databinding.FragmentProductBinding
import id.allana.titipbarangku.ui.product.adapter.ProductAdapter


class ProductFragment : BaseFragment<FragmentProductBinding>(FragmentProductBinding::inflate) {

    private val viewModel: ProductViewModel by viewModels()
    private val productAdapter by lazy {
        ProductAdapter { itemProduct ->
            showAlertDialog(itemProduct)
        }
    }
    companion object {
        private const val TAG = "ProductBottomSheetFragment"
    }

    override fun initView() {
        initRecyclerView()

        getViewBinding().fabAddProduct.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_product_to_productBottomSheetFragment)
        }
    }

    private fun initRecyclerView() {
        getViewBinding().rvProduct.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ProductFragment.productAdapter
        }
    }

    override fun observeData() {
        viewModel.getCategoryWithProduct().observe(viewLifecycleOwner) { listProduct ->
            viewModel.checkDatabaseEmpty(listProduct)
            productAdapter.submitList(listProduct)
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

    private fun showAlertDialog(data: ProductModel) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(R.string.delete_data)
            setMessage(getString(R.string.msg_delete_data, data.name))
            setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteProduct(data)
                Snackbar.make(requireView(), getString(R.string.success_delete_data, data.name), Snackbar.LENGTH_SHORT).show()
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

}