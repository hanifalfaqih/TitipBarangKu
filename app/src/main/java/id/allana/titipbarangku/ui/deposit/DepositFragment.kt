package id.allana.titipbarangku.ui.deposit

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.data.model.ProductModel
import id.allana.titipbarangku.data.model.StoreModel
import id.allana.titipbarangku.databinding.FragmentDepositBinding
import id.allana.titipbarangku.ui.deposit.adapter.DepositAdapter
import id.allana.titipbarangku.ui.product.ProductViewModel
import id.allana.titipbarangku.ui.store.StoreViewModel
import id.allana.titipbarangku.util.snackbar

class DepositFragment : BaseFragment<FragmentDepositBinding>(FragmentDepositBinding::inflate) {

    private val viewModel: DepositViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private val storeViewModel: StoreViewModel by viewModels()
    private val depositAdapter by lazy {
        DepositAdapter()
    }
    private var listProduct = listOf<ProductModel>()
    private var listStore = listOf<StoreModel>()

    override fun initView() {
        initRecyclerView()

        productViewModel.getAllProduct().observe(viewLifecycleOwner) { listProduct = it }
        storeViewModel.getAllStore().observe(viewLifecycleOwner) { listStore = it }

        getViewBinding().fabAddDeposit.setOnClickListener {
            if (listProduct.isNotEmpty() && listStore.isNotEmpty()) {
                findNavController().navigate(R.id.action_navigation_deposit_to_addDepositHolderActivity)
            } else {
                requireView().snackbar(getString(R.string.please_add_product_or_store_first))
            }
        }
    }

    private fun initRecyclerView() {
        getViewBinding().rvDeposit.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@DepositFragment.depositAdapter
        }
    }

    override fun observeData() {
        viewModel.getAllDepositWithStore().observe(viewLifecycleOwner) { list ->
            viewModel.checkDeposit(list)
            depositAdapter.submitList(list)
        }
        viewModel.checkDepositLiveData().observe(viewLifecycleOwner) { isEmpty ->
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