package id.allana.titipbarangku.ui.store

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.data.model.StoreModel
import id.allana.titipbarangku.databinding.FragmentStoreBinding
import id.allana.titipbarangku.ui.store.adapter.StoreAdapter
import id.allana.titipbarangku.util.snackbar

class StoreFragment : BaseFragment<FragmentStoreBinding>(FragmentStoreBinding::inflate) {

    private val viewModel: StoreViewModel by viewModels()
    private val storeAdapter by lazy {
        StoreAdapter { deleteItem ->
            showAlertDialog(deleteItem)
        }
    }

    override fun initView() {
        initRecyclerView()
        getViewBinding().fabAddStore.setOnClickListener {
            val actionToStoreBottomSheet = R.id.action_navigation_store_to_storeBottomSheetFragment
            findNavController().navigate(actionToStoreBottomSheet)
        }
    }

    private fun initRecyclerView() {
        getViewBinding().rvStore.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@StoreFragment.storeAdapter
        }
    }

    private fun showAlertDialog(data: StoreModel) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.delete_data))
            setMessage(getString(R.string.msg_delete_data_store, data.name, data.name, data.name))
            setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteStore(data)
                requireActivity().findViewById<View>(android.R.id.content).snackbar(getString(R.string.success_delete_data, data.name), R.id.fab_add_store)
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    override fun observeData() {
        viewModel.getAllStore().observe(viewLifecycleOwner) { listStore ->
            viewModel.checkDatabaseEmpty(listStore)
            storeAdapter.submitList(listStore)
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