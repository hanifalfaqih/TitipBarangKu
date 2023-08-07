package id.allana.titipbarangku.ui.deposit.add_deposit

import android.app.DatePickerDialog
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import id.allana.titipbarangku.R
import id.allana.titipbarangku.data.base.BaseFragment
import id.allana.titipbarangku.data.model.DepositModel
import id.allana.titipbarangku.data.model.Status
import id.allana.titipbarangku.data.model.StoreModel
import id.allana.titipbarangku.databinding.FragmentAddDepositBinding
import id.allana.titipbarangku.ui.deposit.DepositViewModel
import id.allana.titipbarangku.ui.store.StoreViewModel
import id.allana.titipbarangku.util.ConstantValue.SNACKBAR_DURATION
import id.allana.titipbarangku.util.formatDate
import java.util.Calendar


class AddDepositFragment : BaseFragment<FragmentAddDepositBinding>(FragmentAddDepositBinding::inflate) {

    private val viewModel: DepositViewModel by viewModels()
    private val storeViewModel: StoreViewModel by viewModels()

    private var storeName = arrayListOf<String>()
    private var listStore = listOf<StoreModel>()

    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var spinnerStore: AutoCompleteTextView

    private var startDateDeposit: String = ""
    private var idDeposit: Long = 0L
    private var idStore = 0

    override fun initView() {
        spinnerStore = getViewBinding().textDropdownStore
        spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            storeName
        )
        spinnerStore.setAdapter(spinnerAdapter)

        storeViewModel.getAllStore().observe(viewLifecycleOwner) { list ->
            listStore = list
            for (data in listStore) {
                storeName.add(data.name)
            }
        }

        spinnerStore.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedStore = listStore[position]
            idStore = selectedStore.id
        }

        getViewBinding().btnAddDeposit.text = if (idDeposit == 0L) getString(R.string.add_deposit) else getString(R.string.update_deposit)
        getViewBinding().btnAddDeposit.setOnClickListener {
            insertDeposit(idDeposit, idStore)
        }

        getViewBinding().btnPickDate.setOnClickListener {
            showDatePicker()
        }


        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == android.R.id.home) {
                    findNavController().navigateUp()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun insertDeposit(id: Long, idStore: Int) {
        if (idStore == 0 || getViewBinding().tvStartDateDeposit.text.toString() == getString(R.string.choose_start_date)) {
            Snackbar.make(requireView(), getString(R.string.data_store_and_start_date_must_be_filled), SNACKBAR_DURATION).show()
        } else {
            val deposit = DepositModel(
                id,
                idStore = idStore,
                startDateDeposit = startDateDeposit,
                finishDateDeposit = "",
                status = Status.DEPOSIT
            )

            val successMessage = if (id == 0L) {
                viewModel.insertDeposit(deposit)
                getString(R.string.success_add_deposit)
            } else {
                viewModel.updateDeposit(deposit)
                getString(R.string.success_update_deposit)
            }

            viewModel.idDeposit.observe(viewLifecycleOwner) {
                idDeposit = it
                Snackbar.make(requireView(), successMessage, SNACKBAR_DURATION).show().also {
                    if (findNavController().currentDestination?.id == R.id.addDepositFragment) {
                        val actionToAddProductDeposit = AddDepositFragmentDirections.actionAddDepositFragmentToProductDepositFragment(idDeposit)
                        findNavController().navigate(actionToAddProductDeposit)
                    }
                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(), { _, selectedYear, selectedMonth, dayOfMonth ->
                startDateDeposit = formatDate(selectedYear, selectedMonth, dayOfMonth)
                getViewBinding().tvStartDateDeposit.text = startDateDeposit
            }, year, month, day
        ).show()
    }

    override fun observeData() {}
}