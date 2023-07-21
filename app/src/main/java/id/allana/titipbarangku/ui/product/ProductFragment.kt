package id.allana.titipbarangku.ui.product

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.data.model.ProductWithCategory
import id.allana.titipbarangku.databinding.FragmentProductBinding
import id.allana.titipbarangku.ui.product.adapter.ProductAdapter


class ProductFragment : BaseFragment<FragmentProductBinding>(FragmentProductBinding::inflate) {

    private val viewModel: ProductViewModel by viewModels()
    private val productAdapter by lazy {
        ProductAdapter()
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
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
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

    private fun showAlertDialog(data: ProductWithCategory) {
    }

}