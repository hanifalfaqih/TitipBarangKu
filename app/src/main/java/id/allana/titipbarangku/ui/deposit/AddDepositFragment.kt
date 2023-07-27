package id.allana.titipbarangku.ui.deposit

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
import id.allana.titipbarangku.ui.store.StoreViewModel
import id.allana.titipbarangku.util.formatDate
import java.util.Calendar


class AddDepositFragment : BaseFragment<FragmentAddDepositBinding>(FragmentAddDepositBinding::inflate) {

    private val viewModel: DepositViewModel by viewModels()
    private val storeViewModel: StoreViewModel by viewModels()

    private var storeName = arrayListOf<String>()
    private var listStore = listOf<StoreModel>()
    private var storeId = 0

    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var spinnerStore: AutoCompleteTextView

    private var startDateDeposit: String = ""

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
            spinnerAdapter.notifyDataSetChanged()
        }
        spinnerStore.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedStore = listStore[position]
            storeId = selectedStore.id

        }
        getViewBinding().btnPickDate.setOnClickListener {
            showDatePicker()
        }

        getViewBinding().btnAddDeposit.setOnClickListener {
            insertDeposit(0)
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

    private fun insertDeposit(id: Int) {
        if (validateForm()) {
            val deposit = DepositModel(
                id,
                idStore = storeId,
                startDateDeposit = startDateDeposit,
                finishDateDeposit = "",
                status = Status.DEPOSIT
            )

            val successMessage = if (id == 0) {
                viewModel.insertDeposit(deposit)
                getString(R.string.success_add_deposit)
            } else {
                getString(R.string.success_update_deposit)
            }

            Snackbar.make(requireView(), successMessage, Snackbar.LENGTH_SHORT).show().also {
                this.findNavController().navigate(R.id.action_addDepositFragment_to_productDepositFragment)
            }
        }
    }

    private fun validateForm(): Boolean {
        val textStore = getViewBinding().textDropdownStore.toString()
        val textStartDateDeposit = getViewBinding().tvStartDateDeposit.toString()

        val isFormValid: Boolean

        when {
            textStore.isEmpty() -> {
                isFormValid = false
                getViewBinding().textDropdownStore.error = "Toko harus diisi!"
            }
            textStartDateDeposit.isEmpty() -> {
                isFormValid = false
                getViewBinding().tvStartDateDeposit.error = "Tanggal harus diisi!"
            }
            else -> {
                isFormValid = true
            }
        }
        return isFormValid
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

    override fun observeData() {

    }
}