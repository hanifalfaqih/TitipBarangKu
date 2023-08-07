package id.allana.titipbarangku.ui.deposit

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.data.model.DepositWithStore
import id.allana.titipbarangku.data.model.ProductDepositModel
import id.allana.titipbarangku.data.model.Status
import id.allana.titipbarangku.databinding.FragmentDetailDepositBinding
import id.allana.titipbarangku.ui.deposit.adapter.DetailDepositAdapter
import id.allana.titipbarangku.util.ConstantValue.SNACKBAR_DURATION
import id.allana.titipbarangku.util.formatDate
import id.allana.titipbarangku.util.snackbar
import java.util.Calendar

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
                viewModel.getUpdateStatusDeposit(it.deposit.status)
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
                        args.dataDepositWithStore?.let { depositWithStore ->
                            showAlertDialog(depositWithStore)
                        }
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

        this.detailDepositAdapter.setOnItemClickCallback(object: DetailDepositAdapter.OnItemClickCallback {
            override fun onButtonUpdateQuantity(data: ProductDepositModel, isEmpty: Boolean) {
                if (isEmpty) {
                    requireView().snackbar(getString(R.string.value_cant_empty), R.id.nav_view)
                } else if (data.returnQuantity > data.quantity) {
                    requireView().snackbar(getString(R.string.value_cant_more_than_quantity), R.id.nav_view)
                } else {
                    viewModel.updateProductDeposit(data)
                }
            }
        })
    }

    private fun setDataToView(data: DepositWithStore) {
        getViewBinding().apply {
            tvStoreName.text = data.store?.name
            tvDateDeposit.text = getString(R.string.format_date_deposit, data.deposit.startDateDeposit, data.deposit.finishDateDeposit)
            viewModel.getUpdateStatusDeposit(data.deposit.status)
        }
    }

    override fun observeData() {
        viewModel.getAllProductInDeposit(idDeposit = idDeposit.toInt()).observe(viewLifecycleOwner) { list ->
            viewModel.checkProductInDeposit(list)
            detailDepositAdapter.submitList(list)

            val allTotalProductSoldNotEmpty = list.all { data ->
                data.productDeposit.totalProductSold != "-"
            }
            getViewBinding().btnFinishDeposit.setOnClickListener {
                if (allTotalProductSoldNotEmpty) {
                    val endDateDeposit = Calendar.getInstance()
                    val year = endDateDeposit.get(Calendar.YEAR)
                    val month = endDateDeposit.get(Calendar.MONTH)
                    val dayOfMonth = endDateDeposit.get(Calendar.DAY_OF_MONTH)
                    val date = formatDate(year, month, dayOfMonth)

                    val depositWithNewStatus = args.dataDepositWithStore?.deposit?.copy(status = Status.SELESAI, finishDateDeposit = date)
                    depositWithNewStatus?.let { deposit ->
                        viewModel.getUpdateStatusDeposit(deposit.status)
                        viewModel.updateDeposit(deposit)
                    }
                } else {
                    requireView().snackbar(getString(R.string.value_return_quantity_must_be_filled), R.id.nav_view)
                }
            }
        }
        viewModel.getUpdateStatusDepositLiveData().observe(viewLifecycleOwner) { updateStatusDeposit ->
            if (updateStatusDeposit.name == "DEPOSIT") {
                getViewBinding().tvStatusDeposit.text = getString(R.string.deposit)
                getViewBinding().tvStatusDeposit.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
                getViewBinding().btnFinishDeposit.text = getString(R.string.update_status_deposit)
                getViewBinding().btnFinishDeposit.isEnabled = true
            } else {
                getViewBinding().tvStatusDeposit.text = getString(R.string.finish).uppercase()
                getViewBinding().tvStatusDeposit.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200))
                getViewBinding().btnFinishDeposit.text = getString(R.string.status_deposit_done)
                getViewBinding().btnFinishDeposit.isEnabled = false
            }
        }
        viewModel.checkProductInDepositLiveData().observe(viewLifecycleOwner) { isEmpty ->
            if (isEmpty) {
                stateDataEmpty(true)
            } else {
                stateDataEmpty(false)
            }
        }
    }

    private fun showAlertDialog(data: DepositWithStore) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(getString(R.string.delete_data))
            setMessage(getString(R.string.msg_delete_data_deposit, data.store?.name))
            setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteDeposit(data.deposit)
                Snackbar.make(requireView(), R.string.success_delete_data_deposit, SNACKBAR_DURATION
                ).addCallback(object: Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        if (event == DISMISS_EVENT_TIMEOUT) {
                            // Snackbar selesai ditampilkan
                            findNavController().navigateUp()
                        }
                    }
                }).setAnchorView(R.id.nav_view).show()
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
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