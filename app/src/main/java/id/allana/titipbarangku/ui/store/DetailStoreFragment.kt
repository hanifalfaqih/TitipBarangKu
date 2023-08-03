package id.allana.titipbarangku.ui.store

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.data.model.StoreModel
import id.allana.titipbarangku.databinding.FragmentDetailStoreBinding
import id.allana.titipbarangku.ui.store.adapter.DetailStoreAdapter


class DetailStoreFragment : BaseFragment<FragmentDetailStoreBinding>(FragmentDetailStoreBinding::inflate) {

    private val args by navArgs<DetailStoreFragmentArgs>()

    private val viewModel: StoreViewModel by viewModels()
    private val detailStoreAdapter by lazy {
        DetailStoreAdapter()
    }
    private var idStore = 0

    override fun initView() {
        initRecyclerView()
        args.dataDetailStore?.let { store ->
            idStore = store.id
            setDataToView(store)
        }

        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == android.R.id.home) findNavController().navigateUp()
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initRecyclerView() {
        getViewBinding().rvDepositInStore.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@DetailStoreFragment.detailStoreAdapter
        }
    }

    private fun setDataToView(data: StoreModel) {
        getViewBinding().also {
            it.tvStoreName.text = data.name
            it.tvStoreAddress.text = data.address
            it.tvStoreOwnerName.text = data.ownerName
            it.tvStoreOwnerPhoneNumber.text = data.ownerPhoneNumber
        }
    }

    override fun observeData() {
        viewModel.getAllDepositInStore(idStore = idStore).observe(viewLifecycleOwner) { list ->
            viewModel.checkDepositInStore(list)
            detailStoreAdapter.submitList(list)
        }
        viewModel.checkDepositInStoreLiveData().observe(viewLifecycleOwner) { isEmpty ->
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