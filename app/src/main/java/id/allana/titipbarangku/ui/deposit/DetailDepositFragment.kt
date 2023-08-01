package id.allana.titipbarangku.ui.deposit

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.data.model.DepositWithStore
import id.allana.titipbarangku.databinding.FragmentDetailDepositBinding
import id.allana.titipbarangku.ui.deposit.adapter.DetailDepositAdapter
import id.allana.titipbarangku.util.snackbar

class DetailDepositFragment : BaseFragment<FragmentDetailDepositBinding>(FragmentDetailDepositBinding::inflate) {

    private val args by navArgs<DetailDepositFragmentArgs>()

    private val viewModel: DepositViewModel by viewModels()
    private val detailDepositAdapter by lazy {
        DetailDepositAdapter()
    }
    private var idDeposit = 0L

    override fun initView() {
        initRecyclerView()
        args.dataDepositWithStore.let { depositWithStore ->
            depositWithStore?.let {
                idDeposit = it.deposit.id
                setDataToView(it)
            }
        }

        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_deposit_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    android.R.id.home -> {
                        findNavController().navigateUp()
                    }
                    R.id.menu_delete -> {
                        requireView().snackbar("DELETE")
                    }
                    R.id.menu_finish_deposit -> {
                        requireView().snackbar("FINISH")
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initRecyclerView() {
        getViewBinding().rvProductInDeposit.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = this@DetailDepositFragment.detailDepositAdapter
        }
    }

    private fun setDataToView(data: DepositWithStore) {
        getViewBinding().apply {
            tvStoreName.text = data.store?.name
            tvStartDateDeposit.text = data.deposit.startDateDeposit
            tvEndDateDeposit.text = data.deposit.finishDateDeposit
            tvStatusDeposit.text = data.deposit.status.name
        }
    }

    override fun observeData() {
        viewModel.getAllProductInDeposit(idDeposit = idDeposit.toInt()).observe(viewLifecycleOwner) { list ->
            viewModel.checkProductInDeposit(list)
            detailDepositAdapter.submitList(list)
        }
        viewModel.checkProductInDepositLiveData().observe(viewLifecycleOwner) { isEmpty ->
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